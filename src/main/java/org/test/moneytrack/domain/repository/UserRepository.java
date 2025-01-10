package org.test.moneytrack.domain.repository;

import org.test.moneytrack.domain.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления пользователями.
 */
public interface UserRepository {
    // Найти пользователя по логину
    Optional<User> findByLogin(String login);

    // Сохранить нового пользователя
    void save(User user);

    // Обновить данные пользователя (кошелёк и т.д.)
    void update(User user);

    // Удалить пользователя
    void delete(User user);

    // Все пользователи
    Collection<User> findAll();
}
