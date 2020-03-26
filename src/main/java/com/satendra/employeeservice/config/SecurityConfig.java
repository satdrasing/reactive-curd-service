package com.satendra.employeeservice.config;

import com.satendra.employeeservice.daos.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(it ->
                        it.disable()
                )
                .httpBasic(it -> it.securityContextRepository(new WebSessionServerSecurityContextRepository()))
               // .formLogin(it -> it.securityContextRepository(new WebSessionServerSecurityContextRepository()))
                .authorizeExchange()
                .anyExchange().authenticated()
                .and().build();
    }


    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository users) {
        return (username) -> users.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .authorities(u.getRoles().toArray(new String[0]))
                        .accountExpired(!u.isActive())
                        .credentialsExpired(!u.isActive())
                        .disabled(!u.isActive())
                        .accountLocked(!u.isActive())
                        .build()
                );
    }
    //in memory
  /* @Bean
   public MapReactiveUserDetailsService userDetailsService() {
       UserDetails user = User.withUsername("test").password(passwordEncoder().encode("password")).roles("USER").build();
       UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("USER", "ADMIN").build();
       return new MapReactiveUserDetailsService(user, admin);
   }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
