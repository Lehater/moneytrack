package org.test.moneytrack.domain.service;

import org.test.moneytrack.domain.model.Budget;
import org.test.moneytrack.domain.model.Category;
import org.test.moneytrack.domain.model.Wallet;

import java.math.BigDecimal;

/**
 * Сервис домена, отвечающий за логику работы с бюджетами по категориям.
 */
public class BudgetDomainService {

    /**
     * Создаёт или обновляет бюджет (лимит) для указанной категории в кошельке.
     *
     * @param wallet      кошелёк пользователя
     * @param category    категория, для которой устанавливается лимит
     * @param limitAmount сумма лимита
     * @return созданный или обновлённый объект Budget
     */
    public Budget setBudget(Wallet wallet, Category category, BigDecimal limitAmount) {
        // Вызовем метод из самого кошелька (Wallet), чтобы сохранить согласованность кода
        wallet.setBudget(category, limitAmount);
        return wallet.getBudgets().get(category);
    }

    /**
     * Получить текущий лимит по категории (если он установлен).
     *
     * @param wallet   кошелёк
     * @param category категория
     * @return лимит или null, если нет бюджета для данной категории
     */
    public Budget getBudget(Wallet wallet, Category category) {
        return wallet.getBudgets().get(category);
    }

}
