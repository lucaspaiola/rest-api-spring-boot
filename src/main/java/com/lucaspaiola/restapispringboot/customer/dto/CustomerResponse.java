package com.lucaspaiola.restapispringboot.customer.dto;

import com.lucaspaiola.restapispringboot.customer.enums.Gender;

public record CustomerResponse(
        String id,
        String name,
        String email,
        Gender gender
) {
}
