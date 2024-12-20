package com.pasynekmichal.service;

import com.pasynekmichal.model.CurrencyRequestDTO;
import com.pasynekmichal.model.NbpResponse;
import com.pasynekmichal.model.Rate;
import com.pasynekmichal.model.Request;
import com.pasynekmichal.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CurrencyServiceTest {

    @Value("${currency.api}")
    private String currencyApiUrl;

    @MockitoBean
    private RequestRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
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
    void testCurrencyApiUrl_2() {
        assertEquals("http://api.nbp.pl/api/exchangerates/tables/A?format=json", currencyService.getCurrencyApiUrl());
    }

    @Test
    void shouldReturnCurrencyValue() {
        NbpResponse mockResponse = new NbpResponse();
        CurrencyRequestDTO dto = new CurrencyRequestDTO("USD", "TestCurrency");
        Rate mockRate = new Rate("USD", "USD", 4.0054);

        CurrencyService currencyService = mock(CurrencyService.class);
        when(currencyService.getCurrencyValue(dto)).thenReturn(mockRate.getMid());

        double result = currencyService.getCurrencyValue(dto);

        assertEquals(4.0054, result, 0.0001, "Currency rate should match the mocked value");

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
