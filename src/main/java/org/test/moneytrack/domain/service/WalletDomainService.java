package org.test.moneytrack.domain.service;

import org.test.moneytrack.domain.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Сервис домена, отвечающий за логику работы с кошельком (Wallet).
 * Например, добавление транзакций, проверка баланса, предупреждения о лимитах и т.д.
 */
public class WalletDomainService {

    /**
     * Добавляет операцию (транзакцию) в кошелёк пользователя.
     * Выполняет проверку бюджетных лимитов (если это расход).
     *
     * @param wallet      кошелёк пользователя
     * @param transaction объект транзакции (доход или расход)
     * @return сообщение-предупреждение, если бюджет превышен или баланс ушёл в минус,
     *         иначе пустая строка или null.
     */
    public String addTransaction(Wallet wallet, Transaction transaction) {
        // Добавляем транзакцию в кошелёк
        wallet.addTransaction(transaction);

        // Если это расход, проверяем лимит по категории (если категория не null)
        if (transaction.getType() == TransactionType.EXPENSE && transaction.getCategory() != null) {
            Budget budget = wallet.getBudgets().get(transaction.getCategory());
            if (budget != null) {
                BigDecimal totalSpent = calculateTotalByCategory(wallet, transaction.getCategory(), TransactionType.EXPENSE);
                if (totalSpent.compareTo(budget.getLimitAmount()) > 0) {
                    return String.format("Внимание! Превышен лимит по категории: %s", transaction.getCategory().getName());
                }
            }
        }

        // Проверяем общий баланс на отрицательное значение
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return "Внимание! Баланс кошелька стал отрицательным!";
        }

        // Если нет предупреждений, возвращаем пустую строку или null
        return "";
    }

    /**
     * Подсчитывает сумму (доход или расход) по указанной категории.
     *
     * @param wallet    кошелёк пользователя
     * @param category  категория, по которой нужно подсчитать
     * @param type      тип транзакции (доход или расход)
     * @return общая сумма транзакций заданного типа по категории
     */
    public BigDecimal calculateTotalByCategory(Wallet wallet, Category category, TransactionType type) {
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == type && category.equals(t.getCategory()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Подсчитывает общий доход или расход (по списку категорий).
     *
     * @param wallet     кошелёк пользователя
     * @param categories список категорий
     * @param type       тип транзакции (доход или расход)
     * @return общая сумма всех транзакций заданного типа
     */
    public BigDecimal calculateTotalByCategories(Wallet wallet,
                                                 List<Category> categories,
                                                 TransactionType type) {
        BigDecimal total = BigDecimal.ZERO;
        for (Category cat : categories) {
            // Суммируем результаты по каждому cat
            BigDecimal catSum = calculateTotalByCategory(wallet, cat, type);
            total = total.add(catSum);
        }
        return total;
    }

    /**
     * Подсчитывает общий доход или расход (по всем категориям).
     *
     * @param wallet кошелёк пользователя
     * @param type   тип транзакции (доход или расход)
     * @return общая сумма всех транзакций заданного типа
     */
    public BigDecimal calculateTotal(Wallet wallet, TransactionType type) {
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Ищет транзакцию по её идентификатору (при необходимости).
     *
     * @param wallet        кошелёк
     * @param transactionId идентификатор транзакции
     * @return Optional<Transaction>
     */
    public Optional<Transaction> findTransactionById(Wallet wallet, java.util.UUID transactionId) {
        return wallet.getTransactions().stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst();
    }
}
