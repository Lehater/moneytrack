package org.test.moneytrack.domain.service;

import org.test.moneytrack.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сервис домена, отвечающий за переводы между пользователями (между кошельками).
 * Выполняется дополнительное задание по ТЗ.
 */
public class TransferDomainService {

    private final WalletDomainService walletDomainService;

    public TransferDomainService(WalletDomainService walletDomainService) {
        this.walletDomainService = walletDomainService;
    }

    /**
     * Перевод средств от одного пользователя к другому.
     *
     * @param fromWallet кошелёк отправителя
     * @param toWallet   кошелёк получателя
     * @param amount     сумма перевода
     * @return возможное предупреждение или сообщение (если нужно)
     */
    public String transfer(Wallet fromWallet, Wallet toWallet, BigDecimal amount) {
        // Создадим транзакцию "расход" для отправителя
        Transaction expenseTransaction = new Transaction(
                TransactionType.EXPENSE,
                new Category("Перевод"), // Используем специальную категорию
                amount,
                LocalDateTime.now(),
                "Перевод средств"
        );

        // Создадим транзакцию "доход" для получателя
        Transaction incomeTransaction = new Transaction(
                TransactionType.INCOME,
                new Category("Перевод"), // Аналогичная категория
                amount,
                LocalDateTime.now(),
                "Получение перевода"
        );

        // Сначала списываем у отправителя
        String warning = walletDomainService.addTransaction(fromWallet, expenseTransaction);

        walletDomainService.addTransaction(toWallet, incomeTransaction);

        return warning;
    }
}
