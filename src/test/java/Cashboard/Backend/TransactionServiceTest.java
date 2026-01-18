package Cashboard.Backend;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@CashboardSpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService service;

    @MockitoBean
    private TransactionRepository repo;

    /**
     * Prüft, dass beim Speichern einer Transaktion ohne Wiederholung genau einmal {@code repo.save(...)} aufgerufen wird.
     *
     * @author Daniel91287
     */
    @Test
    void speichernOhneWiederholungRuftRepositorySaveGenauEinmalAuf() {
        String userId = "user-123";

        TransactionRequest req = new TransactionRequest();
        req.setDate(LocalDate.of(2025, 1, 2));
        req.setAmount(-750);
        req.setDescription("Miete");
        req.setRecurrenceInterval(RecurrenceInterval.NON);
        req.setEndDate(null);

        service.save(req, userId);

        verify(repo, times(1)).save(any(Transaction.class));
    }

    /**
     * Prüft, dass beim Speichern ohne Wiederholung die Benutzer-ID gesetzt wird
     * und die übrigen Felder unverändert in die gespeicherte Entity übernommen werden.
     *
     * @author Daniel91287
     */
    @Test
    void speichernOhneWiederholungSetztBenutzerIdUndBehaeltFelder() {
        String userId = "user-123";

        TransactionRequest req = new TransactionRequest();
        req.setDate(LocalDate.of(2025, 1, 2));
        req.setAmount(-750);
        req.setDescription("Miete");
        req.setRecurrenceInterval(RecurrenceInterval.NON);
        req.setEndDate(null);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        service.save(req, userId);

        verify(repo).save(captor.capture());
        Transaction persisted = captor.getValue();

        assertEquals(userId, persisted.getUserId());
        assertEquals(LocalDate.of(2025, 1, 2), persisted.getDate());
        assertEquals(-750, persisted.getAmount());
        assertEquals("Miete", persisted.getDescription());
    }

    /**
     * Prüft, dass bei gesetzter Wiederholung ohne Enddatum eine {@link IllegalArgumentException} geworfen wird,
     * weil der Service ein Enddatum für wiederkehrende Buchungen verlangt.
     *
     * @author Daniel91287
     */
    @Test
    void speichernMitWiederholungOhneEnddatumWirftException() {
        String userId = "user-123";

        TransactionRequest req = new TransactionRequest();
        req.setDate(LocalDate.of(2025, 1, 2));
        req.setAmount(-750);
        req.setDescription("Miete");
        req.setRecurrenceInterval(RecurrenceInterval.MONTHLY);
        req.setEndDate(null);

        assertThrows(IllegalArgumentException.class, () -> service.save(req, userId));
        verify(repo, never()).save(any(Transaction.class));
    }

    /**
     * Prüft den Erfolgsfall für wiederkehrende Buchungen:
     * Bei DAILY und Enddatum = Startdatum + 2 Tage müssen genau 3 Buchungen gespeichert werden
     * (Starttag, +1 Tag, +2 Tage).
     *
     * @author Daniel91287
     */
    @Test
    void speichernMitWiederholungDailyErzeugtMehrereBuchungenBisEnddatumInklusive() {
        String userId = "user-123";

        TransactionRequest req = new TransactionRequest();
        req.setDate(LocalDate.of(2025, 1, 1));
        req.setAmount(10);
        req.setDescription("Kaffee");
        req.setRecurrenceInterval(RecurrenceInterval.DAILY);
        req.setEndDate(LocalDate.of(2025, 1, 3));

        service.save(req, userId);

        // 01.01, 02.01, 03.01 => 3 Saves
        verify(repo, times(3)).save(any(Transaction.class));
    }

    /**
     * Prüft, dass bei wiederkehrenden Buchungen die gespeicherten Datumswerte korrekt fortgeschrieben werden.
     * (Hier: WEEKLY => +1 Woche).
     *
     * @author Daniel91287
     */
    @Test
    void speichernMitWiederholungWeeklySpeichertDieErwartetenDatumswerte() {
        String userId = "user-123";

        TransactionRequest req = new TransactionRequest();
        req.setDate(LocalDate.of(2025, 1, 1));
        req.setAmount(99);
        req.setDescription("Abo");
        req.setRecurrenceInterval(RecurrenceInterval.WEEKLY);
        req.setEndDate(LocalDate.of(2025, 1, 15)); // 01.01, 08.01, 15.01

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        service.save(req, userId);

        verify(repo, times(3)).save(captor.capture());

        assertEquals(LocalDate.of(2025, 1, 1), captor.getAllValues().get(0).getDate());
        assertEquals(LocalDate.of(2025, 1, 8), captor.getAllValues().get(1).getDate());
        assertEquals(LocalDate.of(2025, 1, 15), captor.getAllValues().get(2).getDate());
    }

    /**
     * Prüft, dass das Löschen einer Transaktion an {@code repo.deleteById(id)} delegiert wird.
     *
     * @author Daniel91287
     */
    @Test
    void loeschenDelegiertAnRepositoryDeleteById() {
        long id = 42L;

        service.deleteTransaction(id);

        verify(repo, times(1)).deleteById(id);
    }

    /**
     * Prüft, dass beim Bearbeiten einer existierenden Transaktion die Felder übernommen
     * und anschließend gespeichert werden.
     *
     * @author Daniel91287
     */
    @Test
    void bearbeitenAktualisiertFelderUndSpeichert() {
        long id = 7L;

        Transaction existing = new Transaction();
        existing.setDescription("Alt");
        existing.setAmount(1);
        existing.setDate(LocalDate.of(2025, 1, 1));

        when(repo.findById(id)).thenReturn(Optional.of(existing));

        Transaction updated = new Transaction();
        updated.setDescription("Neu");
        updated.setAmount(123.45);
        updated.setDate(LocalDate.of(2025, 2, 2));

        service.editTransaction(id, updated);

        assertEquals("Neu", existing.getDescription());
        assertEquals(123.45, existing.getAmount());
        assertEquals(LocalDate.of(2025, 2, 2), existing.getDate());

        verify(repo, times(1)).save(existing);
    }

    /**
     * Prüft, dass beim Bearbeiten einer nicht existierenden Transaktion eine Exception geworfen wird.
     *
     * @author Daniel91287
     */
    @Test
    void bearbeitenNichtGefundenWirftException() {
        long id = 999L;

        when(repo.findById(id)).thenReturn(Optional.empty());

        Transaction updated = new Transaction();
        updated.setDescription("Neu");
        updated.setAmount(5);
        updated.setDate(LocalDate.of(2025, 1, 1));

        assertThrows(RuntimeException.class, () -> service.editTransaction(id, updated));
        verify(repo, never()).save(any(Transaction.class));
    }

    /**
     * Prüft, dass alle Transaktionen eines Users über {@code repo.findByUserId(...)} geladen werden.
     *
     * @author Daniel91287
     */
    @Test
    void alleTransaktionenEinesUsersWerdenUeberRepositoryGeladen() {
        String userId = "user-123";

        service.getAllTransactionsForUser(userId);

        verify(repo, times(1)).findByUserId(userId);
    }
}
