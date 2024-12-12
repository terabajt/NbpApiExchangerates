package com.pasynekmichal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasynekmichal.model.CurrencyRequestDTO;
import com.pasynekmichal.model.Request;
import com.pasynekmichal.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCurrentCurrencyValue() throws Exception {
        CurrencyRequestDTO dto = new CurrencyRequestDTO("USD", "John Doe");
        double mockedValue = 4.5;

        when(currencyService.getCurrencyValue(Mockito.any(CurrencyRequestDTO.class))).thenReturn(mockedValue);

        mockMvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(mockedValue));
    }

    @Test
    void testGetAllRequests() throws Exception {

        Request request1 = new Request("USD", "John Doe", LocalDateTime.of(2024, 1, 1, 0, 0), 4.5);
        Request request2 = new Request("EUR", "Jane Doe", LocalDateTime.of(2024, 1, 2, 0, 0), 5.0);
        List<Request> mockedRequests = List.of(request1, request2);

        when(currencyService.getAllRequests()).thenReturn(mockedRequests);

        mockMvc.perform(get("/currencies/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].value").value(4.5))
                .andExpect(jsonPath("$[0].date").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$[1].currency").value("EUR"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$[1].value").value(5.0))
                .andExpect(jsonPath("$[1].date").value("2024-01-02T00:00:00"));
    }


}
