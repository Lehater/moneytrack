package org.test.moneytrack.domain.model;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain entity representing a user's wallet (balance, transactions, budgets).
 */
public class Wallet {

    private BigDecimal balance;
    private final List<Transaction> transactions;
    private final Map<Category, Budget> budgets;

    @JsonCreator
    public Wallet(@JsonProperty("balance") BigDecimal balance,
                  @JsonProperty("transactions") List<Transaction> transactions,
                  @JsonProperty("budgets") Map<Category, Budget> budgets) {
        this.balance = (balance != null) ? balance : BigDecimal.ZERO;
        this.transactions = (transactions != null) ? transactions : new ArrayList<>();
        this.budgets = (budgets != null) ? budgets : new HashMap<>();
    }

    public Wallet() {
        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
    }

    public Wallet(BigDecimal initialBalance) {
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public Map<Category, Budget> getBudgets() {
        // returning unmodifiable map (if you want to keep immutability)
        return Collections.unmodifiableMap(budgets);
    }

    /**
     * Add a transaction to this wallet.
     * This method does not contain domain logic yet (e.g. validations).
     * That can be handled by a domain service or inside a specialized method.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);

        if (transaction.getType() == TransactionType.INCOME) {
            balance = balance.add(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            balance = balance.subtract(transaction.getAmount());
        }
    }

    /**
     * Add or update a budget for a category.
     */
    public void setBudget(Category category, BigDecimal limitAmount) {
        Budget budget = new Budget(category, limitAmount);
        budgets.put(category, budget);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // equals, hashCode, toString - if needed

    @Override
    public String toString() {
        return "Wallet{" +
                "balance=" + balance +
                ", transactions=" + transactions +
                ", budgets=" + budgets +
                '}';
    }
}
