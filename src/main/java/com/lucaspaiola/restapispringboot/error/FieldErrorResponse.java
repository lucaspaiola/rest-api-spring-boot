package com.lucaspaiola.restapispringboot.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
