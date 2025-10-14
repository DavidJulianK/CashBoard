package com.example.demo;

import entitclasses.Transaction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class Controller {

    @GetMapping("/cashboard")
    public List<Transaction> getTransactions() {
        return List.of(
                new Transaction(LocalDate.of(2025, 10, 10)),
                new Transaction(LocalDate.of(2025, 10, 11)),
                new Transaction(LocalDate.of(2025, 10, 12))
        );
    }
}
