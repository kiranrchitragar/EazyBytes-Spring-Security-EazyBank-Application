package com.eazybank.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

//we want filter to be executed only during login operation, once the login is completed we need to generate the JWT Token
// All all next requests we dont need to create token , instead we need to validate the token.
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null!=authentication){
            // Secret key used in signature, this needs tobe stored in secure location.
            String secret = "aD9f8Hk3L0s9P2q8Z4m6T1x7V5w3N9b2";
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            String jwtToken = Jwts.builder().issuer("Easy Bank").subject("JWT Token for Easy Bank")
                    .claim("userName",authentication.getName()) // get username from authentication obj
                    .claim("authorities",authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))// get authorities from authentication
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime()+300000))
                    .signWith(secretKey)
                    .compact();// returns JWT in string format

            System.out.println("jwtToken:: " + jwtToken);
            response.setHeader("JWT_AUTHORIZATION",jwtToken);
        }
        filterChain.doFilter(request,response);
    }

    @Override
    // true -> this filter will not be executed.
    // /user is login, which says that this filter should onlu be executed only on login.
    protected boolean shouldNotFilter(HttpServletRequest request){
        return !request.getServletPath().equals("/user");
    }
}
