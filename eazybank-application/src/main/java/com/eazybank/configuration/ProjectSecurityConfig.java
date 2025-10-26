package com.eazybank.configuration;

import com.eazybank.filters.CSRFCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());*/
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());*/

        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.cors(corsCustomizer->corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                return config;
            }
        }));

/*
        -- Commenting this to check CSRF issues
        http.sessionManagement(smc->smc.invalidSessionUrl("/invalidSession")// Once time out redirect to this page
                .maximumSessions(1)  // Maximum Sessions a user can have. This will invalidate the previous session and new session is cretaed. To avoid this we use below
                .maxSessionsPreventsLogin(true)); // This wont allow for second session to be created till the 1st session is timedout or expired.
 */

        http.requiresChannel(rcc->rcc.anyRequest().requiresInsecure()); // Allows only http calls
        // http.csrf(csrf->csrf.disable());

        // CSRF is applicable only for POST/PUT/DELET calls for private pages- meaning pages visible after login to application
        // A CsrfTokrnRepository persists the CSRF token in a cookie named "XSRF-TOKEN" and reads from header "X-XSRF-TOKEN".
        http.csrf(csrfConfig->csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler) // The CsrfTokenRequestAttributeHandler stores the CSRF token as a request attribute (so it can be accessed from filters or templates
                .ignoringRequestMatchers("/contact","/registerUsers") // Ignore CSRF for public pages, without login
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())); //Save the CSRF token in a cookie (named XSRF-TOKEN by default).Allow JavaScript code (like Angular, React, etc.) to read that token — since HttpOnly is set to false.
        http.addFilterAfter(new CSRFCookieFilter(), BasicAuthenticationFilter.class);

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/user","/notices", "/contact", "/error","/registerUser").permitAll());


        // This tells the Spring, we are not storing the session information in Security Context , while it can save these details in Security COntext.
        http.securityContext(contextConfig->contextConfig.requireExplicitSave(false));

        // We are telling the Spring security framework - Always create a session for us, so we can use this session again & again to access secured API's
        http.sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

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
