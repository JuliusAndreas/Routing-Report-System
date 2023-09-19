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
    public static void main(String[] args) {
        SpringApplication.run(RoutingReportSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}
