package com.pasynekmichal.service;

import com.pasynekmichal.model.CurrencyRequestDTO;
import com.pasynekmichal.model.NbpResponse;
import com.pasynekmichal.model.Rate;
import com.pasynekmichal.model.Request;
import com.pasynekmichal.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
class CurrencyServiceTest {

    @Value("${currency.api}")
    private String currencyApiUrl;

    @Mock
    private RequestRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCurrencyApiUrl() {
        assertEquals("http://api.nbp.pl/api/exchangerates/tables/A?format=json", currencyApiUrl);
    }

    @Test
    void shouldReturnCurrencyValue() {
        // Przygotowanie mockowanej odpowiedzi z API
        NbpResponse mockResponse = new NbpResponse();
        CurrencyRequestDTO dto = new CurrencyRequestDTO("USD", "TestCurrency");
        Rate mockRate = new Rate("USD", "USD", 4.0054);
        mockResponse.setRates(List.of(mockRate));

        // Mockowanie odpowiedzi RestTemplate
        when(restTemplate.getForObject(anyString(), eq(NbpResponse[].class)))
                .thenReturn(new NbpResponse[]{mockResponse});

        // Wywołanie metody getCurrencyValue
        double result = currencyService.getCurrencyValue(dto);

        // Sprawdzamy, czy wynik jest zgodny z oczekiwaną wartością
        assertEquals(4.0054, result, 0.0001, "Currency rate should match the mocked value");

        // Weryfikujemy, że odpowiednia metoda save została wywołana
        verify(repository, times(1)).save(any(Request.class));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotFound() {
        CurrencyRequestDTO dto = new CurrencyRequestDTO("UNKNOWN", "TestCurrency");
        NbpResponse mockResponse = new NbpResponse();
        mockResponse.setRates(List.of(new Rate("USD", "USD", 4.0518)));

        // Mockowanie odpowiedzi RestTemplate
        when(restTemplate.getForObject(anyString(), eq(NbpResponse[].class)))
                .thenReturn(new NbpResponse[]{mockResponse});

        // Sprawdzamy, czy metoda rzuca odpowiedni wyjątek
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyService.getCurrencyValue(dto);
        });

        // Weryfikujemy wiadomość wyjątku
        assertEquals("Currency unknown", exception.getMessage());
    }

    @Test
    void shouldReturnAllRequests() {
        Request mockRequest = new Request();
        mockRequest.setCurrency("USD");
        mockRequest.setValue(4.05);
        mockRequest.setDate(LocalDateTime.now());
        when(repository.findAll()).thenReturn(List.of(mockRequest));

        List<Request> result = currencyService.getAllRequests();

        // Sprawdzamy, czy zwrócona lista nie jest pusta
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getCurrency());
    }
}
