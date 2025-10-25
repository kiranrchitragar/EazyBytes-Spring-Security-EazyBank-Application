//  -- We are disabling this as we are using jdbcUserDetailsManager



//package com.eazybank.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.password.CompromisedPasswordChecker;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//public class InMemoryUserDetailsManagerSecurityConfig {
//
//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());*/
//        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());*/
//        http.csrf(csrf->csrf.disable());
//        http.authorizeHttpRequests((requests) -> requests
//                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
//                .requestMatchers("/notices", "/contact", "/error").permitAll());
//
//        /* Disable form login, if disabled we can use only API's via API's
//           http.formLogin(httpSecurityFormLoginConfigurer -> {
//           httpSecurityFormLoginConfigurer.disable();
//        });
//         */
//
//        /* Disable basic login, if disabled we can use only API's via forms
//           http.httpBasic(httpSecurityHttpBasicConfigurer -> {
//           httpSecurityHttpBasicConfigurer.disable();
//        });
//         */
//        http.formLogin(withDefaults());
//        http.httpBasic(withDefaults());
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//        /*
//        if not using password encoder use {noop} -> .password("{noop}12345")
//
//        https://bcrypt-generator.com/
//        Password is 12345, use the above link to encrpt the pswd
//         */
//        UserDetails user = User.withUsername("user").password("{noop}Blue!Tiger@47Sun").authorities("read").build();
//        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$kDn2lQ/lobS3.Ucwlv6CBeIfDAskRXWSBBv6JIO1pQYTekeI6dBJe").authorities("admin").build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        // By default if we use factories, it uses - BCryptPasswordEncoder
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    // Check if the password is compromised, Check if the password in
//    // weak or is a simple password.
//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker(){
//        // Changing password from 12345-> to strong pasword Blue!Tiger@47Sun
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }
//}
