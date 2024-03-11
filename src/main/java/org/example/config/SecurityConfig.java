package org.example.config;

import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        UserDetails user1 = User.builder()
//                .username("Ivan")
//                .password(passwordEncoder().encode("Postman"))
//                .roles("POSTS")
//                .build();
//        UserDetails user2 = User.builder()
//                .username("Nad")
//                .password(passwordEncoder().encode("Userman"))
//                .roles("USERS")
//                .build();
//        UserDetails user3 = User.builder()
//                .username("Mike")
//                .password(passwordEncoder().encode("Albumman"))
//                .roles("ALBUMS")
//                .build();
//        UserDetails user4 = User.builder()
//                .username("VK")
//                .password(passwordEncoder().encode("Adminman"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2, user3, user4);
//    }

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
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers("/api/posts/**").hasAnyRole("POSTS", "ADMIN")
                .requestMatchers("/api/users/**").hasAnyRole("USERS", "ADMIN")
                .requestMatchers("/api/albums/**").hasAnyRole("ALBUMS", "ADMIN")
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

