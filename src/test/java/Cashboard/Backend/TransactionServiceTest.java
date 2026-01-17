package Cashboard.Backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@CashboardSpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService service;

    @MockitoBean
    private TransactionRepository repo;

    @Test
    void save_setsUserId_andCallsRepoSave() {
        var tx = new Transaction(LocalDate.of(2025, 1, 2), -750, "Miete");

        // repo.save soll einfach das übergebene Objekt zurückgeben
        when(repo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        var saved = service.save(tx, "user-123");

        assertEquals("user-123", saved.getUserId());
        verify(repo, times(1)).save(tx);
    }

    @Test
    void getAllTransactionsForUser_callsRepoFindByUserId() {
        when(repo.findByUserId("user-123"))
                .thenReturn(List.of(new Transaction(LocalDate.of(2025,1,2), 1.0, "X")));

        var result = service.getAllTransactionsForUser("user-123");

        assertEquals(1, result.size());
        verify(repo, times(1)).findByUserId("user-123");
    }
}
