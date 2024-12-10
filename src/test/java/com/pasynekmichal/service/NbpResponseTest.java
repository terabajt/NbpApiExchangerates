package com.pasynekmichal.service;

import com.pasynekmichal.model.Rate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NbpResponseTest {

    @Test
    void testGettersAndSetters() {
        NbpResponse response = new NbpResponse();

        response.setTable("A");
        response.setNo("001/A/NBP/2024");
        response.setEffectiveDate("2024-12-05");

        Rate rate1 = new Rate("USD", "Dolar amerykański", 4.50);
        Rate rate2 = new Rate("EUR", "Euro", 4.80);
        response.setRates(List.of(rate1, rate2));

        assertThat(response.getTable()).isEqualTo("A");
        assertThat(response.getNo()).isEqualTo("001/A/NBP/2024");
        assertThat(response.getEffectiveDate()).isEqualTo("2024-12-05");
        assertThat(response.getRates()).containsExactly(rate1, rate2);
    }

    @Test
    void testEmptyConstructor() {
        NbpResponse response = new NbpResponse();
        assertThat(response.getTable()).isNull();
        assertThat(response.getNo()).isNull();
        assertThat(response.getEffectiveDate()).isNull();
        assertThat(response.getRates()).isNull();
    }
}
