package org.test.moneytrack.domain.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain entity representing a financial Category (e.g. "Food", "Entertainment").
 */
public final class Category {

    private final String name;

    @JsonCreator
    public Category(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // equals and hashCode are important for using Category as a key in Maps
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
//    public String toString() {
//        return "Category{name='" + name + "'}";
//    }
    public String toString() {
        return name;
    }
}
