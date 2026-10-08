package com.lucaspaiola.restapispringboot.customer.service;

import com.lucaspaiola.restapispringboot.customer.document.CustomerDocument;
import com.lucaspaiola.restapispringboot.customer.dto.CreateCustomerRequest;
import com.lucaspaiola.restapispringboot.customer.dto.CustomerResponse;
import com.lucaspaiola.restapispringboot.customer.enums.Gender;
import com.lucaspaiola.restapispringboot.customer.repository.CustomerRepository;
import com.lucaspaiola.restapispringboot.exception.AlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomerSuccessfully() {
        CreateCustomerRequest customerRequest = new CreateCustomerRequest(
                "Lucas Paiola",
                "lucaspaiola@email.com",
                Gender.MALE
        );

        CustomerDocument savedCustomer = new CustomerDocument(
                "customer-123",
                "Lucas Paiola",
                "lucaspaiola@email.com",
                Gender.MALE
        );

        when(customerRepository.existsByEmail(customerRequest.email())).thenReturn(false);
        when(customerRepository.save(any(CustomerDocument.class))).thenReturn(savedCustomer);

        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);

        assertThat(customerResponse.id()).isEqualTo("customer-123");
        assertThat(customerResponse.name()).isEqualTo("Lucas Paiola");
        assertThat(customerResponse.email()).isEqualTo("lucaspaiola@email.com");
        assertThat(customerResponse.gender()).isEqualTo(Gender.MALE);

        verify(customerRepository).existsByEmail("lucaspaiola@email.com");
        verify(customerRepository).save(any(CustomerDocument.class));
    }

    @Test
    void shouldNotCreateCustomerWhenEmailAlreadyExists() {
        CreateCustomerRequest customerRequest = new CreateCustomerRequest(
                "Lucas Paiola",
                "lucaspaiola@email.com",
                Gender.MALE
        );

        when(customerRepository.existsByEmail(customerRequest.email())).thenReturn(true);

        assertThatThrownBy(() ->
                customerService.createCustomer(customerRequest)
        )
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("A customer with this email already exists.");

        verify(customerRepository).existsByEmail("lucaspaiola@email.com");
        verify(customerRepository, never()).save(any(CustomerDocument.class));
    }
}
