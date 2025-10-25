package com.eazybank.configuration;

import com.eazybank.entities.Customer;
import com.eazybank.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EazyBankUserDetailsManager implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public EazyBankUserDetailsManager(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User details not found : " + username));

        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail(),customer.getPwd(),authorityList);
    }
}
