package com.eazybank.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());*/
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());*/
        http.csrf(csrf->csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/v1/myAccount", "/api/v1/myBalance", "/api/v1/myLoans", "/api/v1/myCards").authenticated()
                .requestMatchers("/api/v1/notices", "/api/v1/contact", "/error").permitAll());

        /* Disable form login, if disabled we can use only API's via API's
           http.formLogin(httpSecurityFormLoginConfigurer -> {
           httpSecurityFormLoginConfigurer.disable();
        });
         */

        /* Disable basic login, if disabled we can use only API's via forms
           http.httpBasic(httpSecurityHttpBasicConfigurer -> {
           httpSecurityHttpBasicConfigurer.disable();
        });
         */
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

}
