package com.gymbuddy.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Most specific rules first
                // Public endpoints - accessible without authentication
                .requestMatchers("/", "/pages/**", "/login", "/register", "/api/account/register").permitAll()
                // Static resources - CSS, JS, JSON files
                .requestMatchers("/css/**", "/js/**", "/jsons/**").permitAll()
                
                // Protected feature endpoints - require authentication
                .requestMatchers("/profile", "/profile/**").authenticated()
                .requestMatchers("/exercises", "/exercise/**", "/api/exercises/**").authenticated()
                .requestMatchers("/accounts/**", "/api/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/profile", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                // Redirect to login when access is denied
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login");
                })
                // Redirect to login for access denied (when user is authenticated but lacks permissions)
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/login");
                })
            )
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable()); // Disable HTTP Basic authentication

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}