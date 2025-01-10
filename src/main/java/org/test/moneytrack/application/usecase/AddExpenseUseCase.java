package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.*;
import org.test.moneytrack.domain.repository.UserRepository;
import org.test.moneytrack.domain.service.WalletDomainService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сценарий (Use Case) добавления расхода.
 */
public class AddExpenseUseCase {

    private final UserRepository userRepository;
    private final WalletDomainService walletDomainService;

    public AddExpenseUseCase(UserRepository userRepository,
                             WalletDomainService walletDomainService) {
        this.userRepository = userRepository;
        this.walletDomainService = walletDomainService;
    }

    /**
     * Добавляет расход в кошелёк пользователя.
     *
     * @param user         объект пользователя (авторизованный)
     * @param amount       сумма
     * @param category     категория (обязательна для расхода)
     * @param description  описание
     * @return сообщение о результате (предупреждение или успех)
     */
    public String addExpense(User user,
                             BigDecimal amount,
                             Category category,
                             String description) {
        if (category == null) {
            return "Ошибка: необходимо указать категорию для расхода.";
        }

        Transaction transaction = new Transaction(
                TransactionType.EXPENSE,
                category,
                amount,
                LocalDateTime.now(),
                description
        );

        // Используем доменный сервис для добавления транзакции
        String warning = walletDomainService.addTransaction(user.getWallet(), transaction);

        // Сохраняем изменения
        userRepository.update(user);

        return warning.isEmpty()
                ? "Расход успешно добавлен."
                : "Расход добавлен, предупреждение: " + warning;
    }
}
