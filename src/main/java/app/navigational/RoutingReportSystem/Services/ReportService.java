package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Entities.ReportDomainAttribute;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.ReportMapper;
import app.navigational.RoutingReportSystem.Repositories.*;
import app.navigational.RoutingReportSystem.Utilities.VerifiedType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.locationtech.jts.io.ParseException;
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
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        ReportType reportType = validateReportTypeAndAttributes(reportDTO, userId);
        Report report = reportMapper.fromDTO(reportDTO);
        List<Report> identicalReports = reportRepository.getAllReportsWithGivenParameters(report.getLocation(),
                LocalDateTime.now(), reportType.getId());
        if (identicalReports.isEmpty() || identicalReports == null) {
            return;
        }
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
        return reportMapper.toDTO(reportRepository.getAllNearbyReports(wktLineString, LocalDateTime.now()));
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
