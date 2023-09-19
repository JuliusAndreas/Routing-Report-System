package app.navigational.RoutingReportSystem.Configurations;

import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JTSConfig {
    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}
