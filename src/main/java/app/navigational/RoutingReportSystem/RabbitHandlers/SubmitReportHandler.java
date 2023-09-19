package app.navigational.RoutingReportSystem.RabbitHandlers;

import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Repositories.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Point;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubmitReportHandler {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedissonClient client;

    @RabbitListener(queues = "${queues.reports}", concurrency = "5")
    public void submitReportInParallel(byte[] message) throws IOException {
        Report report = objectMapper.readValue(message, Report.class);
        RLock lock = client.getLock("lock");
        lock.lock();
        reportRepository.save(report);
        lock.unlock();
    }

    public String geoToHash(Point point) {
        return point.toString();
    }
}
