package app.navigational.RoutingReportSystem.Converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Converter(autoApply = true)
public class ChronoUnitConverter implements AttributeConverter<ChronoUnit, String> {
    private List<ChronoUnit> validUnits = List.of(ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS);
    private List<String> validStrings = List.of("Seconds", "Minutes", "Hours");

    @Override
    public String convertToDatabaseColumn(ChronoUnit attribute) {
        if (attribute == null) {
            return null;
        }
        if (!validUnits.contains(attribute)) {
            throw new RuntimeException("Invalid Chrono unit");
        }
        return attribute.toString();
    }

    @Override
    public ChronoUnit convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        if (!validStrings.contains(code)) {
            throw new RuntimeException("Invalid Chrono string");
        }
        switch (code) {
            case "Seconds":
                return ChronoUnit.SECONDS;
            case "Minutes":
                return ChronoUnit.MINUTES;
            case "Hours":
                return ChronoUnit.HOURS;
            default:
                return null;
        }
    }
}
