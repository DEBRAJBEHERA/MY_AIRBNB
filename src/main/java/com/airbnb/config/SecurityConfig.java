package com.airbnb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
     httpSecurity.csrf().disable().cors().disable();
     httpSecurity.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);//it will run our costum filter method first when runnig the application
     httpSecurity.authorizeHttpRequests().anyRequest().permitAll();
 //            .requestMatchers("/api/v1/users/addUser","/api/v1/users/login")
//             .permitAll()
//             .requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")
//             .requestMatchers("/api/v1/users/profile").hasAnyRole("ADMIN","USER")
//             .anyRequest().authenticated();
     return httpSecurity.build();//build is a method it hepls us to build the object(httpsecurity)
    }
}
