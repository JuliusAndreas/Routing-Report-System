package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.AttributeValueDTO;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesValue;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.ValueMapper;
import app.navigational.RoutingReportSystem.Repositories.ReportAttributeKeyRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportAttributeValueRepository;
import app.navigational.RoutingReportSystem.Repositories.ReportTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ReportTypeAttributesValuesService {

    private ReportAttributeKeyRepository attributeKeyRepository;
    private ReportAttributeValueRepository attributeValueRepository;
    private ReportTypeRepository reportTypeRepository;
    private ValueMapper valueMapper;

    @Transactional(rollbackOn = {Exception.class})
    public void addAttributeValue(@NonNull Integer keyId, @NonNull String attributeValueName) {
        Optional<ReportTypeAttributesKey> foundType = attributeKeyRepository.findById(keyId);
        if (foundType.isEmpty()) {
            throw new NotFoundException("Key not found");
        }
        ReportTypeAttributesValue attributesValue = new ReportTypeAttributesValue(attributeValueName, foundType.get());
        attributeValueRepository.save(attributesValue);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void removeAttributeValue(@NonNull Integer valueId) {
        if (!attributeValueRepository.existsById(valueId)) {
            throw new NotFoundException("No value was found to be deleted");
        }
        attributeValueRepository.deleteById(valueId);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void updateAttributeValue(@NonNull Integer valueId, @NonNull AttributeValueDTO attributeValueDTO) {
        Optional<ReportTypeAttributesValue> foundValue = attributeValueRepository.findById(valueId);
        if (foundValue.isEmpty()) {
            throw new NotFoundException("value not found");
        }
        ReportTypeAttributesValue valueToBeUpdated = foundValue.get();
        valueToBeUpdated.setAttributeValue(attributeValueDTO.getAttributeValue());
        attributeValueRepository.save(valueToBeUpdated);
    }
}

