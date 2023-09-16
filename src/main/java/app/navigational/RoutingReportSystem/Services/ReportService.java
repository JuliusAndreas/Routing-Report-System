package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Mappers.ReportMapper;
import app.navigational.RoutingReportSystem.Repositories.ReportDomainAttributeRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportTypeRepository;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReportService {

    private ReportRepository reportRepository;
    private ReportDomainAttributeRepository domainAttributeRepository;
    private ReportMapper reportMapper;
    private ReportTypeRepository typeRepository;
    private UserRepository userRepository;

    @Transactional(rollbackOn = {Exception.class})
    public void submitReport(@NonNull ReportDTO reportDTO, @NonNull Integer userId) {
        Report report = reportMapper.fromDTO(reportDTO);

    }
}
