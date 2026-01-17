package Cashboard.Backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
class CashBoardControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean
    TransactionService service;

    @Test
    void getAllTransactions_returnsUserTransactions() throws Exception {
        var t1 = new Transaction(LocalDate.of(2026, 1, 1), 10.0, "Coffee");
        var t2 = new Transaction(LocalDate.of(2026, 1, 2), 20.0, "Books");

        when(service.getAllTransactionsForUser("user-123")).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/transaction")
                        .with(jwt().jwt(j -> j.subject("user-123"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Coffee"))
                .andExpect(jsonPath("$[1].description").value("Books"));
    }
}

