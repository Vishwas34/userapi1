package com.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN") // Admin can create products
                .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN") // Admin can update products
                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN") // Admin can delete products
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyRole("USER", "ADMIN") // Both user and admin can get products
                .requestMatchers(HttpMethod.GET, "/api/v1/products").hasAnyRole("USER", "ADMIN") // Both user and admin can get all products
                .and()
            .httpBasic() // Enable HTTP Basic authentication
                .and()
            .csrf().disable(); // Disable CSRF for simplicity

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("12345")).roles("ADMIN").build());
        manager.createUser(User.withUsername("user").password(passwordEncoder().encode("123")).roles("USER").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder bean
    }
}
