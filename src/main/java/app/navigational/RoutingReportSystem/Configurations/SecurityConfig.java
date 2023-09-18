package app.navigational.RoutingReportSystem.Configurations;

import app.navigational.RoutingReportSystem.Services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter authFilter;

    // User Creation
    @Bean
    public UserDetailsService userDetailsService() {
        return new JwtUserDetailsService();
    }

    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/users/signup").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/reports/").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/reports/submit").hasAuthority("USER")
                .requestMatchers(HttpMethod.PATCH, "/reports/{reportId}/like").hasAuthority("USER")
                .requestMatchers(HttpMethod.PATCH, "/reports/{reportId}/dislike").hasAuthority("USER")
                .requestMatchers(HttpMethod.GET, "/reports/verifiable").hasAuthority("OPERATOR")
                .requestMatchers(HttpMethod.PATCH, "/reports/{reportId}/verify").hasAuthority("OPERATOR")
                .requestMatchers("/reportTypes/**").hasAuthority("ADMIN")
                .requestMatchers("/users/{userId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/").hasAuthority("ADMIN")
                .and()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}