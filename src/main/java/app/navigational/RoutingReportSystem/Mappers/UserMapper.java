package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "id", source = "user.id")
    UserDTO toDTO(User user);

    @BeanMapping(ignoreByDefault = true)
    List<UserDTO> toDTO(Collection<User> user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "userDTO.username")
    User fromDTO(UserDTO userDTO);
}
