package com.example.demo;

import entityclasses.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/cashboard")
    public Map<String, Object> getTransactions() {
        List<Transaction> transactions = List.of(
                new Transaction(new Date(2025, 11, 19), 10.5, "Einkauf"),
                new Transaction(new Date(2025, 10, 14), 500, "Miete"),
                new Transaction(new Date(2025, 11, 16), 12.7, "Getränke")
        );

        return Map.of(
                "project", "Cashboard Projekt von David Kiedacz und Daniel Porath",
                "transactions", transactions
        );
    }
}
