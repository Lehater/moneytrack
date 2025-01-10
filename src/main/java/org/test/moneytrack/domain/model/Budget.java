package org.test.moneytrack.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain entity representing a spending limit (budget) for a specific Category.
 */
public class Budget {

    private final Category category;
    private final BigDecimal limitAmount;


    @JsonCreator
    public Budget(@JsonProperty("category") Category category,
                  @JsonProperty("limitAmount") BigDecimal limitAmount) {
        this.category = category;
        this.limitAmount = limitAmount;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget)) return false;
        Budget budget = (Budget) o;
        return Objects.equals(category, budget.category) &&
                Objects.equals(limitAmount, budget.limitAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, limitAmount);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "category=" + category +
                ", limitAmount=" + limitAmount +
                '}';
    }
}
