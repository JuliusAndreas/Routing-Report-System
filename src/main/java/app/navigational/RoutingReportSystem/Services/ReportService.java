package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Entities.ReportDomainAttribute;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.ReportMapper;
import app.navigational.RoutingReportSystem.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class ReportService {

    private ReportRepository reportRepository;
    private ReportDomainAttributeRepository domainAttributeRepository;
    private ReportMapper reportMapper;
    private ReportTypeRepository typeRepository;
    private UserRepository userRepository;
    private ReportAttributeValueRepository attributeValueRepository;

    @Transactional(rollbackOn = {Exception.class})
    public void submitReport(@NonNull ReportDTO reportDTO, @NonNull Integer userId) {
        Report report = reportMapper.fromDTO(reportDTO);
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
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
        List<ReportDomainAttribute> domainAttributeList = new ArrayList<>();
        for (Map.Entry<String, String> entry : domainAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ReportDomainAttribute attribute = new ReportDomainAttribute(key, value, report);
            domainAttributeList.add(attribute);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reportExpiration = now.plus(reportType.getDuration(), reportType.getDurationUnit());
        report.setProperties(now, reportExpiration, reportType, foundUser.get(),
                reportType.getVerifiable(), domainAttributeList);
        reportRepository.save(report);
    }
}
