package org.test.moneytrack.infrastructure.repository;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;
import org.test.moneytrack.infrastructure.storage.JsonFileStorage;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Репозиторий, реализующий хранение пользователей в JSON-файлах.
 * Для каждого пользователя создаётся отдельный файл: data_{login}.json
 */
public class FileUserRepository implements UserRepository {

    private final JsonFileStorage jsonFileStorage;

    public FileUserRepository(JsonFileStorage jsonFileStorage) {
        this.jsonFileStorage = jsonFileStorage;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user = jsonFileStorage.loadUser(login);
        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        try {
            // Проверяем, не существует ли уже пользователь с таким логином
            User existing = jsonFileStorage.loadUser(user.getLogin());
            if (existing != null) {
                // Можно выбросить ошибку или просто перезаписать
                // Для учебного проекта - перезапишем
            }
            jsonFileStorage.saveUser(user);
        } catch (IOException e) {
            // Логируем ошибку
            System.err.println("Ошибка сохранения пользователя: " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        try {
            // Просто перезаписываем файл с новыми данными
            jsonFileStorage.saveUser(user);
        } catch (IOException e) {
            System.err.println("Ошибка обновления пользователя: " + e.getMessage());
        }
    }

    @Override
    public void delete(User user) {
        jsonFileStorage.deleteUser(user.getLogin());
    }

    @Override
    public Collection<User> findAll() {
        return Collections.emptyList();
    }

}
