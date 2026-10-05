package com.lucaspaiola.restapispringboot.error;

import java.util.List;

public record ValidationErrorResponse(
        String message,
        List<FieldErrorResponse> errors
) {
}
