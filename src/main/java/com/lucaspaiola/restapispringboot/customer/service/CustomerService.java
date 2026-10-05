package com.lucaspaiola.restapispringboot.customer.service;

import com.lucaspaiola.restapispringboot.customer.dto.CreateCustomerRequest;
import com.lucaspaiola.restapispringboot.customer.dto.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        String id = "1";

        return new CustomerResponse(
                id,
                request.name(),
                request.email(),
                request.gender()
        );
    }
}
