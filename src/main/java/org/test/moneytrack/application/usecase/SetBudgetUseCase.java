package org.test.moneytrack.application.usecase;

import org.test.moneytrack.domain.model.Budget;
import org.test.moneytrack.domain.model.Category;
import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.repository.UserRepository;
import org.test.moneytrack.domain.service.BudgetDomainService;

import java.math.BigDecimal;

/**
 * Сценарий (Use Case) установки лимита (бюджета) для категории.
 */
public class SetBudgetUseCase {

    private final UserRepository userRepository;
    private final BudgetDomainService budgetDomainService;

    public SetBudgetUseCase(UserRepository userRepository,
                            BudgetDomainService budgetDomainService) {
        this.userRepository = userRepository;
        this.budgetDomainService = budgetDomainService;
    }

    /**
     * Устанавливает или обновляет лимит (budget) по категории.
     *
     * @param user       пользователь (авторизованный)
     * @param category   категория расходов
     * @param limitValue значение лимита
     * @return сообщение о результате
     */
    public String setBudget(User user, Category category, BigDecimal limitValue) {
        Budget budget = budgetDomainService.setBudget(user.getWallet(), category, limitValue);

        // Сохраняем изменения
        userRepository.update(user);

        return "Категория: "
                + category.getName()
                + " бюджет: "
                + budget.getLimitAmount();
    }
}
