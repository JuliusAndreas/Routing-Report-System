package app.navigational.RoutingReportSystem;

import app.navigational.RoutingReportSystem.Entities.Role;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import app.navigational.RoutingReportSystem.Utilities.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class RoutingReportSystemApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(RoutingReportSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(userRepository.findAllUsersJoinFetchRoles().get(1).getRoles());
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

    public RoleType setToRoleType(Set<Role> roles) {
        if (roles.contains(new Role(RoleType.ADMIN))) {
            return RoleType.ADMIN;
        } else if (roles.contains(new Role(RoleType.OPERATOR))) {
            return RoleType.OPERATOR;
        } else {
            return RoleType.USER;
        }
    }
}
