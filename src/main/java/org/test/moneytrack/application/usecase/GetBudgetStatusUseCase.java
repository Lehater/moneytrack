package org.test.moneytrack.application.usecase;

import org.test.moneytrack.application.port.OutputRepository;
import org.test.moneytrack.domain.model.*;
import org.test.moneytrack.domain.service.WalletDomainService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Сценарий (Use Case) получения информации по текущим бюджетам.
 */
public class GetBudgetStatusUseCase {

    private final WalletDomainService walletDomainService;
    private final OutputRepository outputRepository;

    public GetBudgetStatusUseCase(WalletDomainService walletDomainService, OutputRepository outputRepository) {
        this.walletDomainService = walletDomainService;
        this.outputRepository = outputRepository;
    }

    /**
     * Формирует сводку по всем категориям, для которых установлен бюджет.
     * Показывает лимит, потрачено и остаток (или превышение).
     */
    public String getBudgetStatus(User user, String outputFile) {
        Map<Category, Budget> budgets = user.getWallet().getBudgets();

        if (budgets.isEmpty()) {
            return "Бюджеты не установлены.";
        }

        StringBuilder sb = new StringBuilder("Состояние бюджетов:\n");
        for (Map.Entry<Category, Budget> entry : budgets.entrySet()) {
            Category category = entry.getKey();
            Budget budget = entry.getValue();
            BigDecimal spent = walletDomainService
                    .calculateTotalByCategory(user.getWallet(), category, TransactionType.EXPENSE);
            BigDecimal remaining = budget.getLimitAmount().subtract(spent);

            sb.append("Категория: ").append(category.getName())
                    .append(", Лимит: ").append(budget.getLimitAmount())
                    .append(", Потрачено: ").append(spent)
                    .append(", Остаток: ").append(remaining)
                    .append("\n");
        }

        String result = sb.toString();

        if (outputFile == null) {
            return result;
        }

        // Если outputFile задан -> пишем в файл
        outputRepository.writeToFile("reports", outputFile, result);

        return "Данные записаны в " + outputFile;
    }
}
