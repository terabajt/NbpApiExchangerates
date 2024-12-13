package com.pasynekmichal.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CurrencyRequestDTO(
        @NotEmpty
        @Size(min = 3, max = 3)
        String currency,

        @NotEmpty
        String name
) {
}
