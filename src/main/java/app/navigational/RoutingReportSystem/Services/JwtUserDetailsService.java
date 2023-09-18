package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Mappers.UserMapper;
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
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO foundUserDTO = userService.getUserEntityByUsername(username);
        if (foundUserDTO == null) {
            throw new RuntimeException("Invalid user");
        }
        User foundUser = userMapper.fromDTOWithFullProperties(foundUserDTO);
        Set<GrantedAuthority> authorities = foundUser
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().getCode()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(foundUser.getUsername()
                , foundUser.getPassword(), authorities);
    }
}
