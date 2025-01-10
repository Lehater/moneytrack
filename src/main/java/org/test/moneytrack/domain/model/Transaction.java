package org.test.moneytrack.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain entity representing a financial transaction (income or expense).
 */
public class Transaction {

    private final UUID transactionId;
    private final TransactionType type;
    private final Category category; // can be null or a "dummy category" for incomes if desired
    private final BigDecimal amount;
    private final LocalDateTime dateTime;
    private final String description;


    @JsonCreator
    public Transaction(@JsonProperty("transactionId") UUID transactionId,
                       @JsonProperty("type") TransactionType type,
                       @JsonProperty("category") Category category,
                       @JsonProperty("amount") BigDecimal amount,
                       @JsonProperty("dateTime") LocalDateTime dateTime,
                       @JsonProperty("description") String description) {
        this.transactionId = (transactionId != null) ? transactionId : UUID.randomUUID();
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.description = description;
    }

    public Transaction(TransactionType type,
                       Category category,
                       BigDecimal amount,
                       LocalDateTime dateTime,
                       String description) {
        this.transactionId = UUID.randomUUID();
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.description = description;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", type=" + type +
                ", category=" + (category != null ? category.getName() : "N/A") +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                '}';
    }
}
