package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @CrossOrigin
    @GetMapping("/transaction")
    public List<Transaction> getAllTransactions() {
        return List.of(
                new Transaction(LocalDate.of(2025, 11, 19), 10.5, "Einkauf"),
                new Transaction(LocalDate.of(2025, 10, 14), 500, "Miete"),
                new Transaction(LocalDate.of(2025, 11, 16), 12.7, "Getränke"),
                new Transaction(LocalDate.of(2025, 11, 19), 10.5, "Einkauf")

        );
    }
}
