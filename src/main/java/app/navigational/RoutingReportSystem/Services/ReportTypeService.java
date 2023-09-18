package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.AttributeKeyDTO;
import app.navigational.RoutingReportSystem.DTOs.ReportTypeDTO;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.KeyMapper;
import app.navigational.RoutingReportSystem.Mappers.ReportTypeMapper;
import app.navigational.RoutingReportSystem.Repositories.ReportTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReportTypeService {

    private ReportTypeRepository reportTypeRepository;
    private ReportTypeMapper reportTypeMapper;
    private KeyMapper keyMapper;

    @Transactional(rollbackOn = {Exception.class})
    public void updateReportType(@NonNull Integer id, @NonNull ReportTypeDTO reportTypeDTO) {
        Optional<ReportType> foundType = reportTypeRepository.findById(id);
        if (foundType.isEmpty()) {
            throw new NotFoundException("Report type not found");
        }
        ReportType reportType = reportTypeMapper.fromDTO(reportTypeDTO);
        reportType.setId(id);
        reportType.setCreatedAt(foundType.get().getCreatedAt());
        reportTypeRepository.save(reportType);
    }

    public List<ReportTypeDTO> getAllReportTypes() {
        List<ReportType> reportTypes = reportTypeRepository.findAll();
        if (reportTypes.isEmpty()) return List.of();
        List<ReportTypeDTO> reportTypeDTOS = new ArrayList<>();
        for (ReportType reportType : reportTypes) {
            ReportTypeDTO temporaryDTO = reportTypeMapper.toDTO(reportType);
            temporaryDTO.setKeys(reportTypeRepository.findAttributeKeysStringsPerType(reportType.getId()));
            reportTypeDTOS.add(temporaryDTO);
        }
        return reportTypeDTOS;
    }

    @Transactional(rollbackOn = {Exception.class})
    public void addReportType(@NonNull ReportTypeDTO reportTypeDTO) {
        ReportType reportType = reportTypeMapper.fromDTO(reportTypeDTO);
        reportType.setCreatedAt(LocalDateTime.now());
        reportTypeRepository.save(reportType);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void removeReportType(@NonNull Integer id) {
        if (!reportTypeRepository.existsById(id)) {
            throw new NotFoundException("No type was found to be deleted");
        }
        reportTypeRepository.deleteById(id);
    }

    public List<AttributeKeyDTO> getAllAttributesKeysPerType(@NonNull Integer id) {
        List<ReportTypeAttributesKey> fetchedKeys = reportTypeRepository.findAttributeKeysPerType(id);
        return keyMapper.toDTO(fetchedKeys);
    }
}
