package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;
import org.test.moneytrack.domain.service.TransferDomainService;

import java.math.BigDecimal;

/**
 * Сценарий (Use Case) перевода средств между пользователями.
 */
public class TransferFundsUseCase {

    private final UserRepository userRepository;
    private final TransferDomainService transferDomainService;

    public TransferFundsUseCase(UserRepository userRepository,
                                TransferDomainService transferDomainService) {
        this.userRepository = userRepository;
        this.transferDomainService = transferDomainService;
    }

    /**
     * Переводит сумму от одного пользователя к другому.
     * Возвращает предупреждения, если они возникли.
     */
    public String transfer(User fromUser, User toUser, BigDecimal amount) {
        // Вызываем доменный сервис перевода
        String warning = transferDomainService.transfer(fromUser.getWallet(), toUser.getWallet(), amount);

        // Сохраняем изменения в репозитории
        userRepository.update(fromUser);
        userRepository.update(toUser);

        return warning.isEmpty()
                ? "Перевод успешно выполнен."
                : "Перевод выполнен, предупреждение: " + warning;
    }
}
