package org.test.moneytrack.infrastructure.repository;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Простая реализация репозитория пользователей в памяти (InMemory).
 * Использует Map<login, User>.
 */
public class InMemoryUserRepository implements UserRepository {

    // Хранилище пользователей в памяти
    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }

    @Override
    public void save(User user) {
        users.put(user.getLogin(), user);
    }

    @Override
    public void update(User user) {
        // В InMemory варианте update можно просто перезаписать
        users.put(user.getLogin(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getLogin());
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
