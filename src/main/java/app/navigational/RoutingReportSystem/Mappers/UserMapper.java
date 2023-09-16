package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    UserDTO toDTO(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "userDTO.username")
    @Mapping(target = "password", source = "userDTO.password")
    User fromDTO(UserDTO userDTO);
}
