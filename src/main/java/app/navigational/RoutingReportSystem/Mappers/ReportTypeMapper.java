package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.ReportTypeDTO;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface ReportTypeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "typeName", source = "reportType.typeName")
    @Mapping(target = "verifiable", source = "reportType.verifiable")
    @Mapping(target = "duration", source = "reportType.duration")
    @Mapping(target = "extensionDuration", source = "reportType.extensionDuration")
    @Mapping(target = "durationUnit", expression = "java(chronoToString(reportType.getDurationUnit()))")
    ReportTypeDTO toDTO(ReportType reportType);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "typeName", source = "reportTypeDTO.typeName")
    @Mapping(target = "verifiable", source = "reportTypeDTO.verifiable")
    @Mapping(target = "duration", source = "reportTypeDTO.duration")
    @Mapping(target = "extensionDuration", source = "reportTypeDTO.extensionDuration")
    @Mapping(target = "durationUnit", expression = "java(stringToChrono(reportTypeDTO.getDurationUnit()))")
    ReportType fromDTO(ReportTypeDTO reportTypeDTO);

    default String chronoToString(ChronoUnit chronoUnit) {
        return chronoUnit.toString();
    }

    default ChronoUnit stringToChrono(String code) {
        switch (code) {
            case "Seconds":
                return ChronoUnit.SECONDS;
            case "Minutes":
                return ChronoUnit.MINUTES;
            case "Hours":
                return ChronoUnit.HOURS;
            default:
                throw new NotFoundException("No such unit is supported");
        }
    }
}
