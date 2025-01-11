package org.test.moneytrack.application.usecase;

import org.test.moneytrack.application.port.OutputRepository;
import org.test.moneytrack.domain.model.Category;
import org.test.moneytrack.domain.model.TransactionType;
import org.test.moneytrack.domain.model.User;
import org.test.moneytrack.domain.service.WalletDomainService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Use Case: показать (посчитать) сумму расходов по нескольким категориям.
 * Пример команды: show_expenses Еда Развлечения ...
 */
public class ShowExpensesUseCase {

    private final WalletDomainService walletDomainService;
    private final OutputRepository outputRepository;

    public ShowExpensesUseCase(WalletDomainService walletDomainService,
                               OutputRepository outputRepository) {
        this.walletDomainService = walletDomainService;
        this.outputRepository = outputRepository;
    }

    /**
     * @param user          - текущий авторизованный пользователь
     * @param categoryNames - список названий категорий
     * @param outputFile    - имя файла для вывода (или null, если надо вывести в консоль)
     * @return строка с результатом (если outputFile == null), либо "" (если записано в файл)
     */
    public String execute(User user,
                          List<String> categoryNames,
                          String outputFile) {
        // 1) Преобразуем названия в объекты Category
        List<Category> categories = new ArrayList<>();
        for (String catName : categoryNames) {
            categories.add(new Category(catName));
        }

        // 2) Посчитаем для каждой категории индивидуально + общий итог
        StringBuilder sb = new StringBuilder("Расходы по категориям:\n");
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (Category cat : categories) {
            // Подсчёт по одной категории
            BigDecimal catSum = walletDomainService.calculateTotalByCategory(
                    user.getWallet(), cat, TransactionType.EXPENSE);

            if (catSum.compareTo(BigDecimal.ZERO) == 0) {
                // Уведомляем, что по этой категории нет расходов
                // (по логике ТЗ - "Если категория не найдена, уведомлять")
                sb.append(" - Категория '")
                        .append(cat.getName())
                        .append("' не найдена или нет расходов.\n");
            } else {
                sb.append(" - ")
                        .append(cat.getName())
                        .append(": ")
                        .append(catSum)
                        .append("\n");
                grandTotal = grandTotal.add(catSum);
            }
        }

        sb.append("\nИтого по выбранным категориям: ").append(grandTotal);

        // 3) Если нужен вывод в файл
        if (outputFile != null) {
            outputRepository.writeToFile("reports", outputFile, sb.toString());
            return "Данные записаны в " + outputFile;
        } else {
            // Иначе вернём как строку
            return sb.toString();
        }
    }
}
