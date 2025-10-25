package com.eazybank.controller;

import com.eazybank.entities.Customer;
import com.eazybank.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer){
        try{
            String hashPswd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPswd);
            Customer savedCustomer = customerRepository.save(customer);
            if(savedCustomer!=null && savedCustomer.getId()>0){
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("User Details saved successfullly");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User Registration Failed");
            }
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception Occurred : " + ex.getMessage());
        }

    }
}
