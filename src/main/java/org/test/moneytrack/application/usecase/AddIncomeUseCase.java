package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.*;
import org.test.moneytrack.domain.repository.UserRepository;
import org.test.moneytrack.domain.service.WalletDomainService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сценарий (Use Case) добавления дохода пользователю.
 */
public class AddIncomeUseCase {

    private final UserRepository userRepository;
    private final WalletDomainService walletDomainService;

    public AddIncomeUseCase(UserRepository userRepository,
                            WalletDomainService walletDomainService) {
        this.userRepository = userRepository;
        this.walletDomainService = walletDomainService;
    }

    /**
     * Добавляет доход в кошелёк авторизованного пользователя.
     *
     * @param user       объект пользователя (авторизованный)
     * @param amount     сумма дохода
     * @param category   категория (может быть null или специальная "Без категории")
     * @param description описание
     * @return сообщение о результате (предупреждение или успех)
     */
    public String addIncome(User user,
                            BigDecimal amount,
                            Category category,
                            String description) {

        // Создаём транзакцию типа INCOME
        Transaction transaction = new Transaction(
                TransactionType.INCOME,
                category,   // можно передавать null или dummy категорию
                amount,
                LocalDateTime.now(),
                description
        );

        // Добавляем транзакцию через доменный сервис
        String warning = walletDomainService.addTransaction(user.getWallet(), transaction);

        // Сохраняем изменения в репозитории
        userRepository.update(user);

        return warning.isEmpty()
                ? "Доход успешно добавлен."
                : "Доход добавлен, предупреждение: " + warning;
    }
}
