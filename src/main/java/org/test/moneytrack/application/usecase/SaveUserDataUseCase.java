package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;

/**
 * Use Case: сохранить (обновить) данные пользователя (который есть в памяти) в файл.
 */
public class SaveUserDataUseCase {

    private final UserRepository fileRepository;

    public SaveUserDataUseCase(UserRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Сохраняет данные пользователя в fileRepository.
     *
     * @param user пользователь (со всеми изменениями), который надо сохранить
     */
    public void saveUserToFile(User user) {
        fileRepository.update(user);
    }
}
