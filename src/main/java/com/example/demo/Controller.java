package com.example.demo;

import entityclasses.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/cashboard")
    public Map<String, Object> getTransactions() {
        List<Transaction> transactions = List.of(
                new Transaction(LocalDate.of(2025, 10, 10)),
                new Transaction(LocalDate.of(2025, 10, 11)),
                new Transaction(LocalDate.of(2025, 10, 12))
        );

        return Map.of(
                "project", "Cashboard Projekt von David Kiedacz und Daniel Porath",
                "transactions", transactions
        );
    }
}
