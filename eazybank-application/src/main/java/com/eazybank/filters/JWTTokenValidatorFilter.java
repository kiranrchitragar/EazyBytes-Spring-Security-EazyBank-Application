package com.eazybank.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader("JWT_AUTHORIZATION");
        System.out.println("jwtToken:: read :: " + jwtToken);
        if(null!=jwtToken){
            // Secret key used in signature, this needs tobe stored in secure location.
            String secret = "aD9f8Hk3L0s9P2q8Z4m6T1x7V5w3N9b2";
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            try {

                // Validate the JWT token and create UAthentication.

                // AS this filter is executed before BasicAuthenticationFilter, Spring security will not authenticate
                // the user one more time,
                Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken).getPayload();
                String userName = claims.get("userName").toString();
                String authorities = claims.get("authorities").toString();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userName,null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex){
                throw new BadCredentialsException("Invalid Token received");
            }

        }
        filterChain.doFilter(request,response);
    }

    @Override
    // true -> this filter will not be executed.
    // THis filter will be executed for all requests , except /user
    protected boolean shouldNotFilter(HttpServletRequest request){
        return request.getServletPath().equals("/user");
    }
}
