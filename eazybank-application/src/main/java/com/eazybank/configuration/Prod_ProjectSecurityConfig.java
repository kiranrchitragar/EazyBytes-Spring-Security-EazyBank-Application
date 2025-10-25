package com.eazybank.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class Prod_ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());*/
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());*/
        http.csrf(csrf->csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error","/registerUser").permitAll());

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

//      As we are using Custom Userdetails manager, we need to comment this - EazyBankUserDetailsManager
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        // By default if we use factories, it uses - BCryptPasswordEncoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
