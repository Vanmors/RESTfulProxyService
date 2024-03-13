package org.example.config;

import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(this.userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    protected SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(HttpMethod.GET, "/api/posts/**").hasAnyRole("POSTS_VIEWER", "POSTS_EDITOR", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/albums/**").hasAnyRole("ALBUMS_VIEWER", "USERS_EDITOR", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USERS_VIEWER", "ALBUMS_EDITOR", "ADMIN")
                .requestMatchers("/api/posts/**").hasAnyRole("POSTS_EDITOR", "ADMIN")
                .requestMatchers("/api/users/**").hasAnyRole("USERS_EDITOR", "ADMIN")
                .requestMatchers("/api/albums/**").hasAnyRole("ALBUMS_EDITOR", "ADMIN")
                .requestMatchers("/ws").hasRole("POSTS_EDITOR")
                .requestMatchers("/role").permitAll()
                .requestMatchers("/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

