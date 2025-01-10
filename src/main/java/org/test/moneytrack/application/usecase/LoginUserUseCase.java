package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;

import java.util.Optional;

/**
 * Сценарий (Use Case) авторизации пользователя.
 */
public class LoginUserUseCase {

    private final UserRepository userRepository;

    public LoginUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Авторизует пользователя по логину и паролю.
     * Возвращает объект User или null, если авторизация не удалась.
     */
    public User login(String login, String password) {

        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isEmpty()) {
            return null; // Пользователь не найден
        }

        User user = userOpt.get();
        // Простейшая проверка пароля (в учебном проекте пароль хранится в открытом виде)
        if (!user.getPassword().equals(password)) {
            return null; // Неверный пароль
        }

        // Если в реальном проекте - тут бы происходила загрузка кошелька из файла
        // или что-то подобное.

        return user;
    }
}
