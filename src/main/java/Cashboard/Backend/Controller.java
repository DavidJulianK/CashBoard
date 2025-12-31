package Cashboard.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@CrossOrigin/*(origins = {
        "https://cashboard-frontend-upuk.onrender.com",
        "http://localhost:5173"
}) */
@RestController
public class Controller {

    @Autowired
    private TransactionService service;

    @GetMapping("/transaction")
    public List<Transaction> getAllTransactions(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return service.getAllTransactionsForUser(userId);
    }

    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Transaction transaction, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return service.save(transaction, userId);
    }
}