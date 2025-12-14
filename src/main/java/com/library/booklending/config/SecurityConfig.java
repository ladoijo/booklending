package com.library.booklending.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private static final List<LibraryUser> LIBRARY_USERS = new ArrayList<>(
      List.of(
          new LibraryUser("userlibrarian", "userlibrarian1", "LIBRARIAN"),
          new LibraryUser("usermember", "usermember1", "MEMBER")
      )
  );

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/",
                "/favicon.ico",
                "/actuator/health/**"
            ).permitAll()
            .requestMatchers("/api/v*/members/**").hasRole("LIBRARIAN")
            .requestMatchers(HttpMethod.GET, "/api/v*/books/**").hasRole("MEMBER")
            .requestMatchers("/api/v*/books/**").hasRole("LIBRARIAN")
            .requestMatchers("/api/v*/loans/**").hasAnyRole("MEMBER", "LIBRARIAN")
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder encoder) {
    var manager = new InMemoryUserDetailsManager();
    for (var libraryUser : LIBRARY_USERS) {
      manager.createUser(
          User.builder()
              .username(libraryUser.username)
              .password(encoder.encode(libraryUser.password))
              .roles(libraryUser.role)
              .build()
      );
    }
    return manager;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private record LibraryUser(
      String username,
      String password,
      String role
  ) {

  }
}
