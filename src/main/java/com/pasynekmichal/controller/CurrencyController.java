package com.pasynekmichal.controller;

import com.pasynekmichal.model.CurrencyRequestDTO;
import com.pasynekmichal.model.Request;
import com.pasynekmichal.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("currencies")
public class CurrencyController {
    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @PostMapping("get-current-currency-value-command")
    public ResponseEntity<?> getCurrentCurrencyValue(@Valid @RequestBody CurrencyRequestDTO dto) {
        double value = service.getCurrencyValue(dto);
        return ResponseEntity.ok().body(Map.of("value", value));
    }

    @GetMapping("requests")
    public ResponseEntity<List<Request>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }
}
