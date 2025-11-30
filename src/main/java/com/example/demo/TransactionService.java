package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repo;

    public Transaction Save(Transaction transaction) {
        return repo.save(transaction);
    }

    public Transaction getTransactionById(long id) {
        return repo.findById(id).orElse(null);
    }

    //public Transaction updateTransactionByName(long id) {
    //    return repo.findById(id).;
    //}
}
