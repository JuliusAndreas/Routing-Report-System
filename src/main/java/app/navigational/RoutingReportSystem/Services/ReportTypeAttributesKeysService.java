package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.AttributeKeyDTO;
import app.navigational.RoutingReportSystem.DTOs.AttributeValueDTO;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesValue;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.KeyMapper;
import app.navigational.RoutingReportSystem.Mappers.ValueMapper;
import app.navigational.RoutingReportSystem.Repositories.ReportAttributeKeyRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportAttributeValueRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReportTypeAttributesKeysService {

    private ReportAttributeKeyRepository attributeKeyRepository;
    private ReportAttributeValueRepository attributeValueRepository;
    private ReportTypeRepository reportTypeRepository;
    private KeyMapper keyMapper;
    private ValueMapper valueMapper;

    @Transactional(rollbackOn = {Exception.class})
    public void addAttributeKey(@NonNull Integer reportTypeId, @NonNull AttributeKeyDTO attributeKeyDTO) {
        Optional<ReportType> foundType = reportTypeRepository.findById(reportTypeId);
        if (foundType.isEmpty()) {
            throw new NotFoundException("Report type not found");
        }
        ReportTypeAttributesKey attributesKey = new ReportTypeAttributesKey(attributeKeyDTO.getAttributeKey(),
                foundType.get());
        attributeKeyRepository.save(attributesKey);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void removeAttributeKey(@NonNull Integer keyId) {
        if (!attributeKeyRepository.existsById(keyId)) {
            throw new NotFoundException("No key was found to be deleted");
        }
        attributeKeyRepository.deleteById(keyId);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void updateAttributeKey(@NonNull Integer keyId, @NonNull AttributeKeyDTO attributeKeyDTO) {
        Optional<ReportTypeAttributesKey> foundKey = attributeKeyRepository.findById(keyId);
        if (foundKey.isEmpty()) {
            throw new NotFoundException("key not found");
        }
        ReportTypeAttributesKey keyToBeUpdated = foundKey.get();
        keyToBeUpdated.setAttributeKey(attributeKeyDTO.getAttributeKey());
        attributeKeyRepository.save(keyToBeUpdated);
    }

    public List<AttributeValueDTO> getAllValuesForAKey(@NonNull Integer id) {
        List<ReportTypeAttributesValue> allValuesForTheKey = attributeValueRepository.findValuesByKeyId(id);
        return valueMapper.toDTO(allValuesForTheKey);
    }
}
