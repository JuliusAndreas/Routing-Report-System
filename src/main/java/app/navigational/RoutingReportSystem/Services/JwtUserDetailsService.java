package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.Cache.UserCacheManager;
import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.Role;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.UserWithRolesMapper;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import app.navigational.RoutingReportSystem.Utilities.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCacheManager userCacheManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserWithRolesMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO foundUserDTO = userCacheManager.getUserByUsernameCached(username);
        if (foundUserDTO == null) {
            User foundUser = userRepository.findByUsernameJoinFetch(username);
            if (foundUser == null) {
                throw new NotFoundException("User not found");
            }
            foundUserDTO = userMapper.toDTOWithRolesLoaded(foundUser);
        }
        User foundUser = userMapper.fromDTO(foundUserDTO);
        foundUser.setPassword(foundUserDTO.getPassword());
        foundUser.setRoles(roleTypeToSet(foundUserDTO.getRoleType()));
        Set<GrantedAuthority> authorities = foundUser
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().getCode()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(foundUser.getUsername()
                , foundUser.getPassword(), authorities);
    }

    public Set<Role> roleTypeToSet(RoleType roleType) {
        if (roleType == RoleType.ADMIN) {
            return Set.of(new Role(RoleType.ADMIN), new Role(RoleType.OPERATOR), new Role(RoleType.USER));
        } else if (roleType == RoleType.OPERATOR) {
            return Set.of(new Role(RoleType.OPERATOR), new Role(RoleType.USER));
        } else {
            return Set.of(new Role(RoleType.USER));
        }
    }
}
