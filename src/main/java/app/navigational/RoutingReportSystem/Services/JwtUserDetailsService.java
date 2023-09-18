package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
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
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsernameJoinFetch(username);
        if (foundUser == null) {
            throw new RuntimeException("Invalid user");
        }
        Set<GrantedAuthority> authorities = foundUser
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().getCode()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(foundUser.getUsername()
                , foundUser.getPassword(), authorities);
    }
}
