package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.AttributeKeyDTO;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface KeyMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeKey", source = "reportTypeAttributesKey.attributeKey")
    AttributeKeyDTO toDTO(ReportTypeAttributesKey reportTypeAttributesKey);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeKey", source = "attributeKeyDTO.attributeKey")
    ReportTypeAttributesKey fromDTO(AttributeKeyDTO attributeKeyDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeKey", source = "keys.attributeKey")
    List<AttributeKeyDTO> toDTO(Collection<ReportTypeAttributesKey> keys);
}
