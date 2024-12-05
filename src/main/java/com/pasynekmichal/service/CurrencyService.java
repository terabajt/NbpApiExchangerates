package com.pasynekmichal.service;

import com.pasynekmichal.model.CurrencyRequestDTO;
import com.pasynekmichal.model.Request;
import com.pasynekmichal.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyService {

    private final RequestRepository repository;
    private final RestTemplate restTemplate;

    public CurrencyService(RequestRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public double getCurrencyValue(CurrencyRequestDTO dto) {
        String url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

        NbpResponse[] response = restTemplate.getForObject(url, NbpResponse[].class);
        if (response != null && response.length > 0) {
            double value = response[0].getRates().stream()
                    .filter(rate -> rate.getCode().equalsIgnoreCase(dto.getCurrency()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Waluta nieznaleziona"))
                    .getMid();

            Request request = new Request();
            request.setCurrency(dto.getCurrency());
            request.setName(dto.getName());
            request.setDate(LocalDateTime.now());
            request.setValue(value);

            repository.save(request);

            return value;
        }
        throw new IllegalArgumentException("Nie udało się pobrać danych z API NBP");
    }

    public List<Request> getAllRequests() {
        return repository.findAll();
    }
}
