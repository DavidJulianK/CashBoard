package Cashboard.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repo;

    public void save(TransactionRequest request, String userId) {

        LocalDate currentDate = request.getDate();
        LocalDate endDate = request.getEndDate();

        if (request.getRecurrenceInterval() != RecurrenceInterval.NON
                && request.getEndDate() == null) {
            throw new IllegalArgumentException("Enddatum erforderlich bei Wiederholung");
        }

        if (request.getRecurrenceInterval() == RecurrenceInterval.NON) {
            saveSingleTransaction(request, userId, currentDate);
            return;
        }

        while (endDate == null || !currentDate.isAfter(endDate)) {
            saveSingleTransaction(request, userId, currentDate);
            currentDate = nextDate(currentDate, request.getRecurrenceInterval());
        }
    }

    private void saveSingleTransaction(
            TransactionRequest request,
            String userId,
            LocalDate date
    ) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setDate(date);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        repo.save(transaction);
    }

    private LocalDate nextDate(LocalDate date, RecurrenceInterval interval) {
        return switch (interval) {
            case DAILY -> date.plusDays(1);
            case WEEKLY -> date.plusWeeks(1);
            case MONTHLY -> date.plusMonths(1);
            case QUARTERLY -> date.plusMonths(3);
            case YEARLY -> date.plusYears(1);
            default -> throw new IllegalArgumentException("Unknown interval");
        };
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
