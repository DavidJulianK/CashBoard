package Cashboard.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repo;

    public Transaction save(Transaction transaction) {
        return repo.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        Iterable<Transaction> iterator = repo.findAll();
        List<Transaction> transactions = new ArrayList<Transaction>();
        for (Transaction t : iterator) {
            transactions.add(t);
        }
        return transactions;
    }

}
