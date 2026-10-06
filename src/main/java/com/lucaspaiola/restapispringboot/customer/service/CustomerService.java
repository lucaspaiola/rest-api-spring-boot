package com.lucaspaiola.restapispringboot.customer.service;

import com.lucaspaiola.restapispringboot.customer.document.CustomerDocument;
import com.lucaspaiola.restapispringboot.customer.dto.CreateCustomerRequest;
import com.lucaspaiola.restapispringboot.customer.dto.CustomerResponse;
import com.lucaspaiola.restapispringboot.customer.repository.CustomerRepository;
import com.lucaspaiola.restapispringboot.exception.AlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        if(customerRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("A customer with this email already exists.");
        }

        CustomerDocument customer = new CustomerDocument(
                null,
                request.name(),
                request.email(),
                request.gender()
        );

        CustomerDocument savedCustomer = customerRepository.save(customer);

        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getGender()
        );
    }
}
