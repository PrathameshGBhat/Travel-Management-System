// CustomerController.java
package com.cts.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.CreateCustomerDto;
import com.cts.dto.UpdateCustomerDto;
import com.cts.entities.Customer;
import com.cts.entities.Location;
import com.cts.service.CustomerService;
import com.cts.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/TravelAgency/Customer")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> add(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        Customer createdCustomer = customerService.create(createCustomerDto);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> update(@Valid @PathVariable Long id, @Valid @RequestBody UpdateCustomerDto updateCustomerDto){
        Customer updatedCustomer = customerService.update(id, updateCustomerDto);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.CREATED); 
    }
    
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id){ 
        customerService.delete(id);
        return new ResponseEntity<String>("Accepted",HttpStatus.ACCEPTED);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getById(id));
    }
    
    @GetMapping("/customers") 
    public ResponseEntity<List<Customer>> getAll(){
        return ResponseEntity.ok(customerService.getAll());
    }
    
    
}