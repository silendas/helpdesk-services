package com.cms.helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cms.helpdesk.common.exception.JwtException;
import com.cms.helpdesk.config.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final JwtException jwtException;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtException))
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/api/authenticate")
                                                .permitAll()
                                                .requestMatchers("/api/users/register")
                                                .permitAll()
                                                .requestMatchers("/api/otp/**")
                                                .permitAll()
                                                .requestMatchers("/api/attachment/**")
                                                .permitAll()
                                                .requestMatchers("/api/users/forgotpwd")
                                                .permitAll()
                                                .requestMatchers("/api/employee/validate")
                                                .permitAll()
                                                .requestMatchers("/forgotpwdui/**")
                                                .permitAll()
                                                .requestMatchers("/forgotpasssubmit")
                                                .permitAll()
                                                .requestMatchers("/swagger-ui/index.html")
                                                .permitAll()
                                                .requestMatchers("/WEB-INF/jsp/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.OPTIONS, "/**")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
