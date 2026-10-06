package com.lucaspaiola.restapispringboot.customer.repository;

import com.lucaspaiola.restapispringboot.customer.document.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerDocument, String> {

    boolean existsByEmail(String email);
}
