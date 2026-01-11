package Cashboard.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repo;

    public void save(Transaction transaction, String userId) {
        transaction.setUserId(userId);
        repo.save(transaction);
    }

    public List<Transaction> getAllTransactionsForUser(String userId) {
        return repo.findByUserId(userId);
    }

    public void deleteTransaction(long id) {
        repo.deleteById(id);
    }

    public void editTransaction(long id, Transaction updated) {

        Transaction existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        existing.setDescription(updated.getDescription());
        existing.setAmount(updated.getAmount());
        existing.setDate(updated.getDate());

        repo.save(existing);
    }

}
