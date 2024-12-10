package com.pasynekmichal.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CurrencyRequestDTO {
    @NotEmpty
    @Size(min = 3, max = 3)
    private String currency;
    @NotEmpty
    private String name;

    public CurrencyRequestDTO() {
    }

    public CurrencyRequestDTO(String currency, String name) {
        this.currency = currency;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
