package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.Role;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Utilities.RoleType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "userDTO.username")
    @Mapping(target = "password", source = "userDTO.password")
    @Mapping(target = "roles", expression = "java(roleTypeToSet(userDTO.getRoleType()))")
    User fromDTOWithFullProperties(UserDTO userDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "roleType", expression = "java(setToRoleType(user.roles))")
    UserDTO toDTOWithRolesLoaded(User user);

    @BeanMapping(ignoreByDefault = true)
    List<UserDTO> toDTOWithRolesLoaded(Collection<User> users);

    default RoleType setToRoleType(Set<Role> roles) {
        if (roles.contains(new Role(RoleType.ADMIN))) {
            return RoleType.ADMIN;
        } else if (roles.contains(new Role(RoleType.OPERATOR))) {
            return RoleType.OPERATOR;
        } else {
            return RoleType.USER;
        }
    }

    default Set<Role> roleTypeToSet(RoleType roleType) {
        if (roleType == RoleType.ADMIN) {
            return Set.of(new Role(RoleType.ADMIN), new Role(RoleType.OPERATOR), new Role(RoleType.USER));
        } else if (roleType == RoleType.OPERATOR) {
            return Set.of(new Role(RoleType.OPERATOR), new Role(RoleType.USER));
        } else {
            return Set.of(new Role(RoleType.USER));
        }
    }
}
