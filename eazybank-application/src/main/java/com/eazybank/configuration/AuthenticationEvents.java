package com.eazybank.configuration;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent){
        String userName = authenticationSuccessEvent.getAuthentication().getName();
        System.out.println("Authentication success for user : " + userName);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent abstractAuthenticationFailureEvent){
        System.out.println("Authentication Failed : " + abstractAuthenticationFailureEvent.getException().getMessage());
    }
}
