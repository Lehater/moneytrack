package org.test.moneytrack.application.usecase;

import org.test.moneytrack.application.port.OutputRepository;
import org.test.moneytrack.domain.model.Transaction;
import org.test.moneytrack.domain.model.TransactionType;
import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.model.Wallet;
import org.test.moneytrack.domain.service.WalletDomainService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сценарий (Use Case) получения общей статистики (доход, расход, баланс).
 */
public class GetSummaryUseCase {

    private final WalletDomainService walletDomainService;
    private final OutputRepository outputRepository;

    public GetSummaryUseCase(WalletDomainService walletDomainService,
                             OutputRepository outputRepository) {
        this.walletDomainService = walletDomainService;
        this.outputRepository = outputRepository;
    }

    /**
     * Возвращает строку отчёта и/или пишет её в файл (если outputFile != null).
     */
    public String getSummary(User user, String outputFile) {

        Wallet wallet = user.getWallet();

        // 1) Общий доход / расход
        BigDecimal totalIncome = walletDomainService.calculateTotal(wallet, TransactionType.INCOME);
        BigDecimal totalExpense = walletDomainService.calculateTotal(wallet, TransactionType.EXPENSE);

        // 2) Доходы по категориям (если доходы имеют категории)
        // Соберём все INCOME-транзакции, сгруппируем по category.name и суммируем
        Map<String, BigDecimal> incomeByCategory = wallet.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .collect(Collectors.groupingBy(
                        t -> (t.getCategory() == null)
                                ? "Без категории"
                                : t.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        // 3) Бюджеты по категориям:
        //    - для каждой категории из wallet.getBudgets()
        //    - считаем потраченное = calculateTotalByCategory(..., EXPENSE)
        //    - считаем остаток = budgetLimit - потрачено
        StringBuilder budgetsInfo = new StringBuilder();
        wallet.getBudgets().forEach((cat, budget) -> {
            BigDecimal spent = walletDomainService.calculateTotalByCategory(wallet, cat, TransactionType.EXPENSE);
            BigDecimal leftover = budget.getLimitAmount().subtract(spent);
            budgetsInfo.append(cat.getName())
                    .append(": ")
                    .append(budget.getLimitAmount())
                    .append(", Оставшийся бюджет: ")
                    .append(leftover)
                    .append("\n");
        });

        // Сформируем итоговую строку для вывода:
        StringBuilder sb = new StringBuilder();
        // Пример вывода:
        sb.append("Общий доход: ").append(totalIncome).append("\n");

        sb.append("Доходы по категориям:\n");
        // Пробежимся по карте incomeByCategory
        for (Map.Entry<String, BigDecimal> entry : incomeByCategory.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        sb.append("Общие расходы: ").append(totalExpense).append("\n");

        sb.append("Бюджет по категориям:\n");
        sb.append(budgetsInfo);

        String result = sb.toString().trim();

        // Если не задан файл, возвращаем строку (для вывода в консоль)
        if (outputFile == null) {
            return result;
        }

        // Иначе записываем в файл (папка "reports", файл outputFile)
        outputRepository.writeToFile("reports", outputFile, result);

        // Возвращаем короткое сообщение
        return "Данные записаны в " + outputFile + ": \n";
    }
}
