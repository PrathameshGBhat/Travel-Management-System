package com.cts.service;


import java.util.List;

import com.cts.dto.CreateCustomerDto;
import com.cts.dto.UpdateCustomerDto;
import com.cts.entities.Customer;

public interface CustomerService {
	List<Customer> getAll(); 
	Customer getById(Long id);
	Customer update(Long id,UpdateCustomerDto updateCustomerDto);
	void delete(Long id);
	Customer create(CreateCustomerDto createCustomerDto);
}
