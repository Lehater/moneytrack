package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;

import java.util.Optional;

/**
 * Use Case: загрузить данные пользователя из файла (fileRepository)
 * и сохранить их в inMemoryRepository (если пользователь найден).
 *
 * Данный сценарий часто используется при login,
 * когда нужно подтянуть свежие данные из файла в память.
 */
public class LoadUserDataUseCase {

    private final UserRepository fileRepository;
    private final UserRepository inMemoryRepository;

    public LoadUserDataUseCase(UserRepository fileRepository, UserRepository inMemoryRepository) {
        this.fileRepository = fileRepository;
        this.inMemoryRepository = inMemoryRepository;
    }

    /**
     * Загружает пользователя из fileRepository (по логину),
     * при успехе сохраняет в inMemoryRepository.
     *
     * @param login логин пользователя
     * @return true, если пользователь найден в файле и успешно загружен в память, иначе false
     */
    public boolean loadUserToMemory(String login) {
        Optional<User> maybeUser = fileRepository.findByLogin(login);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            inMemoryRepository.save(user);
            return true;
        }
        return false;
    }
}
