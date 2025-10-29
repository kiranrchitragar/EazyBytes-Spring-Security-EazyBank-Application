package com.eazybank.repository;

import com.eazybank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.authorities WHERE c.email = :email")
    Optional<Customer> findByEmail(String email);

}
