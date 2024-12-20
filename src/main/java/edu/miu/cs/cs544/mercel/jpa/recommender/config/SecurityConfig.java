package edu.miu.cs.cs544.mercel.jpa.recommender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // In-memory UserDetailsService with two users: user:user and admin:admin
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // Add user with role USER
        manager.createUser(
                User.withUsername("user")
                        .password(passwordEncoder().encode("user"))
                        .roles("USER") // Assign USER role
                        .build()
        );

        // Add admin with role ADMIN
        manager.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN") // Assign ADMIN role
                        .build()
        );

        return manager;
    }

    // Password Encoder configuration
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security Filter Chain with CSRF disabled
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/api/**","/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")  // Restrict /api/** to ADMIN role
                .requestMatchers("/user/**").hasRole("USER")  // Restrict /user/** to USER role
                .anyRequest().authenticated()  // All other paths require authentication
                .and()
                .httpBasic()  // Enable HTTP Basic Authentication
                .and()
                .csrf().disable();  // Disable CSRF protection

        return http.build(); // Return the security configuration
    }
}
