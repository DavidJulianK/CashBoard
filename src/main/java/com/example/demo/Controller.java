package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {
        "https://cashboard-frontend-upuk.onrender.com",
        "http://localhost:5173"
})
@RestController
public class Controller {

    @Autowired
    private TransactionService service;

    @GetMapping("/transaction")
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return service.save(transaction);
    }
}



/*package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    TransactionService service;

    @CrossOrigin
    @GetMapping("/transaction")
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    @CrossOrigin
    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return service.save(transaction);
    }
}*/