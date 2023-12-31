package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.Configurations.RabbitConfig;
import app.navigational.RoutingReportSystem.DTOs.RabbitSubmitReportDTO;
import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Entities.ReportDomainAttribute;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.ReportMapper;
import app.navigational.RoutingReportSystem.Projections.ReportSimpleView;
import app.navigational.RoutingReportSystem.Repositories.ReportAttributeValueRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportTypeRepository;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import app.navigational.RoutingReportSystem.Utilities.VerifiedType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.locationtech.jts.io.ParseException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class ReportService {

    private ReportRepository reportRepository;
    private ReportMapper reportMapper;
    private ReportTypeRepository typeRepository;
    private UserRepository userRepository;
    private ReportAttributeValueRepository attributeValueRepository;
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;
    private RedissonClient client;

    @Transactional(rollbackOn = {Exception.class})
    public void submitReport(@NonNull ReportDTO reportDTO, @NonNull Integer userId) throws JsonProcessingException {
        RLock lock = client.getLock("lock");
        lock.lock();
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        ReportType reportType = validateReportTypeAndAttributes(reportDTO, userId);
        Report report = reportMapper.fromDTO(reportDTO);
        List<ReportSimpleView> identicalReports = reportRepository.getAllReportsWithGivenParameters(report.getLocation(),
                reportType.getId());
        if (!identicalReports.isEmpty()) {
            return;
        }
        byte[] message = objectMapper.writeValueAsBytes(new RabbitSubmitReportDTO(reportDTO, userId));
        rabbitTemplate.convertAndSend(RabbitConfig.REPORTS_EXCHANGE_NAME,
                "data.report", message);
        lock.unlock();
    }

    @Transactional(rollbackOn = {Exception.class})
    public void verifyReport(@NonNull Integer reportId) {
        Report report = reportRepository.findNotVerifiedByIdJoinFetchType(reportId);
        if (report == null) {
            throw new NotFoundException("Such report does not exist or is already verified");
        }
        report.setVerified(VerifiedType.VERIFIED);
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setExpiresAt(now.plus(report.getReportType().getDuration(), report.getReportType().getDurationUnit()));
        reportRepository.save(report);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void likeReport(@NonNull Integer reportId) {
        Optional<Report> foundReport = reportRepository.findById(reportId);
        if (foundReport.isEmpty()) {
            throw new NotFoundException("Such report does not exist");
        }
        Report report = foundReport.get();
        report.incrementLikes();
        reportRepository.save(report);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void dislikeReport(@NonNull Integer reportId) {
        Optional<Report> foundReport = reportRepository.findById(reportId);
        if (foundReport.isEmpty()) {
            throw new NotFoundException("Such report does not exist");
        }
        Report report = foundReport.get();
        report.incrementDislikes();
        reportRepository.save(report);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void deleteReport(@NonNull Integer id) {
        if (!reportRepository.existsById(id)) {
            throw new NotFoundException("No Report was found to be deleted");
        }
        reportRepository.deleteById(id);
    }

    public List<ReportDTO> getVerifiableReports() {
        return reportMapper.toDTO(reportRepository.getAllUnverifiedReports());
    }

    public List<ReportDTO> getAllNearbyActiveReports(@NonNull String wktLineString) throws ParseException {
        List<Integer> reportIdsList = reportRepository.getAllNearbyReports(wktLineString);
        List<Report> fetchedReports = reportRepository.getReportsJoinFetchTypeAttributesByIdList(reportIdsList);
        List<ReportDTO> reportDTOList = reportMapper.toDTO(fetchedReports);
        return reportDTOList;
    }

    public ReportType validateReportTypeAndAttributes(ReportDTO reportDTO, Integer userId) {
        ReportType reportType = typeRepository.findReportTypeByTypeName(reportDTO.getReportTypeName());
        if (reportType == null) {
            throw new NotFoundException("Unknown report type");
        }
        Map<String, String> domainAttributes = reportDTO.getDomainAttributes();
        Set<String> keys = domainAttributes.keySet();
        Set<String> availableKeys = typeRepository.
                findAttributeKeysSetPerType(reportType.getId());
        if (!keys.stream().
                allMatch(availableKeys::contains)) {
            throw new NotFoundException("Unknown attribute key(s)");
        }
        if (!keys.stream()
                .allMatch(key -> attributeValueRepository.findValuesByKeyName(key)
                        .contains(domainAttributes.get(key)))) {
            throw new NotFoundException("Unknown attribute value(s)");
        }
        return reportType;
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
