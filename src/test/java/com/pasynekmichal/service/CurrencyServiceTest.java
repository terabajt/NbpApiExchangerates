package com.pasynekmichal.service;

import com.pasynekmichal.model.CurrencyRequestDTO;
import com.pasynekmichal.model.Rate;
import com.pasynekmichal.model.Request;
import com.pasynekmichal.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

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
    void shouldReturnCurrencyValueAndSaveRequest() {
        CurrencyRequestDTO dto = new CurrencyRequestDTO("USD", "TestCurrency");
        NbpResponse mockResponse = new NbpResponse();
        Rate mockRate = new Rate("USD", "USD", 4.0518);
        mockResponse.setRates(List.of(mockRate));

        when(restTemplate.getForObject(anyString(), eq(NbpResponse[].class)))
                .thenReturn(new NbpResponse[]{mockResponse});

        when(repository.save(any(Request.class))).thenReturn(new Request());

        double result = currencyService.getCurrencyValue(dto);

        assertEquals(4.05, result, 0.01, "Currency rate should be rounded to two decimal places");

        verify(repository, times(1)).save(any(Request.class));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotFound() {
        CurrencyRequestDTO dto = new CurrencyRequestDTO("UNKNOWN", "TestCurrency");
        NbpResponse mockResponse = new NbpResponse();
        mockResponse.setRates(List.of(new Rate("USD", "USD", 4.0518)));

        when(restTemplate.getForObject(anyString(), eq(NbpResponse[].class)))
                .thenReturn(new NbpResponse[]{mockResponse});

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyService.getCurrencyValue(dto);
        });

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

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getCurrency());
    }
}
