package Cashboard.Backend;

import java.time.LocalDate;

public class TransactionRequest {

    private LocalDate date;
    private double amount;
    private String description;

    private RecurrenceInterval recurrenceInterval;
    private LocalDate endDate;

    public TransactionRequest() {
    }

    public TransactionRequest(LocalDate date, double amount, String description, RecurrenceInterval recurrenceInterval, LocalDate endDate) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.recurrenceInterval = recurrenceInterval;
        this.endDate = endDate;
    }

    public TransactionRequest(LocalDate date, double amount, String description, RecurrenceInterval recurrenceInterval) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.recurrenceInterval = recurrenceInterval;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecurrenceInterval getRecurrenceInterval() {
        return recurrenceInterval;
    }

    public void setRecurrenceInterval(RecurrenceInterval recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

