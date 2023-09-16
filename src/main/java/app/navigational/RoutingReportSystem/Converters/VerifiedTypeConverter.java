package app.navigational.RoutingReportSystem.Converters;

import app.navigational.RoutingReportSystem.Utilities.VerifiedType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class VerifiedTypeConverter implements AttributeConverter<VerifiedType, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(VerifiedType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public VerifiedType convertToEntityAttribute(Boolean code) {
        if (code == null) {
            return null;
        }

        return Stream.of(VerifiedType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}