package Cashboard.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@CrossOrigin(origins = {
        "https://cashboard-frontend-upuk.onrender.com",
        "http://localhost:5173"
})
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
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        service.save(request, userId);
    }


    @DeleteMapping("/transaction/{id}")
    public void deleteTransaction(@PathVariable long id, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        service.deleteTransaction(id);
    }

    @PutMapping("/transaction/{id}")
    public void editTransaction(@PathVariable long id, @RequestBody Transaction transaction, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        service.editTransaction(id, transaction);
    }
}