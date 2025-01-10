package org.test.moneytrack.domain.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Domain entity representing a user (login, password, single Wallet).
 */
public class User {

    private final String login;
    private final String password;
    private final Wallet wallet;

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("password") String password,
                @JsonProperty("wallet") Wallet wallet) {
        this.login = login;
        this.password = password;
        this.wallet = wallet;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = new Wallet();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
