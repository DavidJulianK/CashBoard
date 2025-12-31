package Cashboard.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repo;

    public Transaction save(Transaction transaction, String userId) {
        transaction.setUserId(userId);
        return repo.save(transaction);
    }

    public List<Transaction> getAllTransactionsForUser(String userId) {
        return repo.findByUserId(userId);
    }

}
