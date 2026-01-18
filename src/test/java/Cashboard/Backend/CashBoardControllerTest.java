package Cashboard.Backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller-Tests für die REST-Endpoints.
 *
 * @author Daniel91287
 */
@WebMvcTest(Controller.class)
@Import(CashBoardControllerTest.JacksonTestConfig.class)
class CashBoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private TransactionService service;

    /**
     * Prüft, dass GET /transaction den User aus dem JWT liest und den Service passend aufruft.
     *
     * @author Daniel91287
     */
    @Test
    void getTransaktionenLiestUserIdAusJwtUndRuftServiceAuf() throws Exception {
        var auth = authMitSubject("user-123");

        when(service.getAllTransactionsForUser("user-123"))
                .thenReturn(List.of(new Transaction(LocalDate.of(2025, 1, 1), 1.0, "X")));

        mvc.perform(get("/transaction").with(authentication(auth)))
                .andExpect(status().isOk());

        verify(service, times(1)).getAllTransactionsForUser("user-123");
    }

    /**
     * Prüft, dass POST /transaction den User aus dem JWT liest,
     * den Request annimmt und {@code 201 CREATED} zurückgibt.
     *
     * @author Daniel91287
     */
    @Test
    void postTransaktionRuftServiceSaveAufUndGibtCreatedZurueck() throws Exception {
        var auth = authMitSubject("user-123");

        TransactionRequest req = new TransactionRequest();
        req.setDate(LocalDate.of(2025, 1, 2));
        req.setAmount(-750);
        req.setDescription("Miete");
        req.setRecurrenceInterval(RecurrenceInterval.NON);
        req.setEndDate(null);

        mvc.perform(post("/transaction")
                        .with(authentication(auth))
                        .with(csrf())
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(service, times(1)).save(any(TransactionRequest.class), eq("user-123"));
    }

    /**
     * Prüft, dass DELETE /transaction/{id} den Service mit der ID aufruft.
     * (Hinweis: userId wird im aktuellen Controller zwar gelesen, aber nicht verwendet.)
     *
     * @author Daniel91287
     */
    @Test
    void deleteTransaktionRuftServiceDeleteAuf() throws Exception {
        var auth = authMitSubject("user-123");

        mvc.perform(delete("/transaction/42")
                        .with(authentication(auth))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteTransaction(42L);
    }

    /**
     * Prüft, dass PUT /transaction/{id} den Service mit ID und Body aufruft.
     * (Hinweis: userId wird im aktuellen Controller zwar gelesen, aber nicht verwendet.)
     *
     * @author Daniel91287
     */
    @Test
    void putTransaktionRuftServiceEditAuf() throws Exception {
        var auth = authMitSubject("user-123");

        Transaction updated = new Transaction();
        updated.setDate(LocalDate.of(2025, 2, 2));
        updated.setAmount(123.45);
        updated.setDescription("Neu");

        mvc.perform(put("/transaction/7")
                        .with(authentication(auth))
                        .with(csrf())
                        .contentType("application/json")
                        .content(om.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(service, times(1)).editTransaction(eq(7L), any(Transaction.class));
    }

    // --- Helper: Authentication mit Jwt als Principal bauen ---

    /**
     * Erstellt ein {@link JwtAuthenticationToken} mit einem JWT, dessen {@code sub}-Claim gesetzt ist.
     * Das wird benötigt, damit {@code @AuthenticationPrincipal Jwt jwt} im Controller korrekt befüllt wird.
     *
     * @author Daniel91287
     * @param subject Wert für den {@code sub}-Claim (entspricht der UserId).
     * @return Authentication-Token, das im MockMvc-Request genutzt werden kann.
     */
    private AbstractAuthenticationToken authMitSubject(String subject) {
        Jwt jwt = new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("sub", subject)
        );

        // Authorities sind hier egal, aber Spring erwartet eine nicht-null Collection.
        return new JwtAuthenticationToken(jwt, AuthorityUtils.NO_AUTHORITIES);
    }

    /**
     * Mini-Konfiguration für einen {@link ObjectMapper} im {@link WebMvcTest}-Slice,
     * damit Java-Time (LocalDate) korrekt serialisiert/deserialisiert wird.
     *
     * @author Daniel91287
     */
    @TestConfiguration
    static class JacksonTestConfig {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }
}
