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
        http.sessionManagement(smc->smc.invalidSessionUrl("/invalidSession")// Once time out redirect to this page
                .maximumSessions(1)  // Maximum Sessions a user can have. This will invalidate the previous session and new session is cretaed. To avoid this we use below
                .maxSessionsPreventsLogin(true)); // This wont allow for second session to be created till the 1st session is timedout or expired.

        http.requiresChannel(rcc->rcc.anyRequest().requiresSecure()); // Allows only https calls - redirects to 8443[default port of https]
        http.csrf(csrf->csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error","/registerUser","/login/**","/invalidSession").permitAll());

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

        http.formLogin(flc->flc.loginPage("/login") // our customized login page,if this is not mentioned, it will display login page from spring security login page-> post
                .defaultSuccessUrl("/dashboard") // once logged in its always redirected to Default
                .failureUrl("/login?error=true")); // Once Authentication is failed, we will redirect to error page


        http.logout(logot->logot.logoutSuccessUrl("/loginn?logout=true")  //Default loggut URL
                .invalidateHttpSession(true) // Invalidate the https session once logged out.
                .clearAuthentication(true) // ANy Authentication object in Security context is cleared.
                .deleteCookies("JSESSIONID")); // Delete cookie once logged out

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
