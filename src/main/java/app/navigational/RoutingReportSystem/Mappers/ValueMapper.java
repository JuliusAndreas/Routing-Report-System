package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.AttributeValueDTO;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesValue;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ValueMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeValue", source = "reportTypeAttributesValue.attributeValue")
    AttributeValueDTO toDTO(ReportTypeAttributesValue reportTypeAttributesValue);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeValue", source = "attributeValueDTO.attributeValue")
    ReportTypeAttributesValue fromDTO(AttributeValueDTO attributeValueDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeValue", source = "values.attributeValue")
    List<AttributeValueDTO> toDTO(Collection<ReportTypeAttributesValue> values);
}
