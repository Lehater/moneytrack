package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;

import java.util.Optional;

/**
 * Сценарий (Use Case) регистрации нового пользователя.
 */
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Регистрирует пользователя.
     * Возвращает текстовое сообщение об успехе или ошибке (если логин уже существует).
     */
    public String register(String login, String password) {
        // Проверяем, не занят ли логин
        Optional<User> existingUser = userRepository.findByLogin(login);
        if (existingUser.isPresent()) {
            return "Ошибка: такой логин уже существует.";
        }

        // Создаём нового пользователя и сохраняем
        User newUser = new User(login, password);
        userRepository.save(newUser);

        return "Пользователь " + login + " успешно зарегистрирован.";
    }
}
