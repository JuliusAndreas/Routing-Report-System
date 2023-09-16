package app.navigational.RoutingReportSystem.Mappers;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {
    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public KeyMapper keyMapper() {
        return Mappers.getMapper(KeyMapper.class);
    }

    @Bean
    public ValueMapper valueMapper() {
        return Mappers.getMapper(ValueMapper.class);
    }

    @Bean
    public ReportTypeMapper reportTypeMapper() {
        return Mappers.getMapper(ReportTypeMapper.class);
    }
}
