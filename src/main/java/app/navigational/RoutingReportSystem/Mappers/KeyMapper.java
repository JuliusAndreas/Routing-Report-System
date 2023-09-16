package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.AttributeKeyDTO;
import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import app.navigational.RoutingReportSystem.Entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KeyMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeKey", source = "reportTypeAttributesKey.attributeKey")
    AttributeKeyDTO toDTO(ReportTypeAttributesKey reportTypeAttributesKey);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeKey", source = "attributeKeyDTO.attributeKey")
    ReportTypeAttributesKey fromDTO(AttributeKeyDTO attributeKeyDTO);
}
