package app.navigational.RoutingReportSystem.RabbitHandlers;

import app.navigational.RoutingReportSystem.Configurations.RabbitConfig;
import app.navigational.RoutingReportSystem.DTOs.RabbitSubmitReportDTO;
import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Entities.ReportDomainAttribute;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Mappers.ReportMapper;
import app.navigational.RoutingReportSystem.Repositories.ReportRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportTypeRepository;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Point;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SubmitReportHandler {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedissonClient client;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ReportTypeRepository typeRepository;
    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = "${queues.reports}", concurrency = "5")
    public void submitReportInParallel(byte[] message) throws IOException {
        RabbitSubmitReportDTO rabbitSubmitReportDTO = objectMapper.
                readValue(message, RabbitSubmitReportDTO.class);
        RLock lock = client.getLock("lock");
        lock.lock();
        ReportDTO reportDTO = rabbitSubmitReportDTO.getReportDTO();
        Integer userId = rabbitSubmitReportDTO.getUserId();
        Report report = reportMapper.fromDTO(reportDTO);
        Optional<User> foundUser = userRepository.findById(userId);
        ReportType reportType = typeRepository.findReportTypeByTypeName(reportDTO.getReportTypeName());
        List<ReportDomainAttribute> domainAttributeList = attributesMapToList(
                reportDTO.getDomainAttributes(), report);
        if (reportType.getVerifiable()) {
            report.setPropertiesForVerifiableReport(reportType, foundUser.get(), domainAttributeList);
            reportRepository.save(report);
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reportExpiration = now.plus(reportType.getDuration(), reportType.getDurationUnit());
        report.setPropertiesForNonVerifiableReport(now, reportExpiration, reportType, foundUser.get(),
                domainAttributeList);
        reportRepository.save(report);
        lock.unlock();
    }

    public String geoToHash(Point point) {
        return point.toString();
    }

    public List<ReportDomainAttribute> attributesMapToList(Map<String, String> domainAttributes, Report report) {
        List<ReportDomainAttribute> domainAttributeList = new ArrayList<>();
        for (Map.Entry<String, String> entry : domainAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ReportDomainAttribute attribute = new ReportDomainAttribute(key, value, report);
            domainAttributeList.add(attribute);
        }
        return domainAttributeList;
    }
}
