package org.test.moneytrack.interfaces.controller;

import org.test.moneytrack.config.ApplicationConfig;
import org.test.moneytrack.domain.model.Category;
import org.test.moneytrack.domain.model.User;

import java.math.BigDecimal;
import java.util.List;


/**
 * Контроллер, который обрабатывает "командный" уровень:
 * - Проверяет корректность аргументов,
 * - Вызывает нужные Use Cases,
 * - Формирует человекочитаемые сообщения (для CLI).
 */
public class MoneyTrackController {

    private final ApplicationConfig config;

    // Текущий залогиненный пользователь
    private User currentUser;

    public MoneyTrackController(ApplicationConfig config) {
        this.config = config;
    }

    // ---------------- Команды аутентификации ----------------

    public String register(String login, String password) {
        if (login.length() < 3 || password.length() < 3) {
            return "Ошибка: логин и пароль должны быть не короче 3 символов.";
        }
        return config.getRegisterUserUseCase().register(login, password);
    }

    public String login(String login, String password) {
        if (currentUser != null) { //TODO: перенести всю бизнес-логику в юз кейсы
            logout();
        }
        // 1) Загружаем данные из файла в память
        boolean loaded = config.getLoadUserDataUseCase().loadUserToMemory(login);

        // 2) Логинимся по inMemory
        User user = config.getLoginUserUseCase().login(login, password);
        if (user == null) {
            return "Ошибка: неверный логин или пароль.";
        }
        currentUser = user;
        return "Успешная авторизация. Текущий пользователь: " + currentUser.getLogin();
    }

    public String logout() {
        if (currentUser == null) {
            return "Ошибка: никто не залогинен.";
        }
        // Сохраняем всех пользователей в памяти; //TODO: перенести всю бизнес-логику в юз кейсы
        for (User u : config.getInMemoryRepository().findAll()) {
            config.getSaveUserDataUseCase().saveUserToFile(u);
        }
        String loggedOut = currentUser.getLogin();
        currentUser = null;
        return "Пользователь " + loggedOut + " разлогинен, данные сохранены.";
    }

    // ---------------- Прочие методы ----------------

    public String addIncome(String amountStr, String categoryStr) {
        if (!isUserLoggedIn()) return "Сначала авторизуйтесь.";

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            Category category = (categoryStr != null) ? new Category(categoryStr) : null;

            String result = config.getAddIncomeUseCase().addIncome(
                    currentUser, amount, category, "доход по CLI");
            return result;
        } catch (NumberFormatException e) {
            return "Ошибка: некорректная сумма.";
        }
    }

    public String addExpense(String amountStr, String categoryStr) {
        if (!isUserLoggedIn()) return "Сначала авторизуйтесь.";

        if (categoryStr == null) {
            return "Ошибка: необходимо указать категорию.";
        }
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            String result = config.getAddExpenseUseCase().addExpense(
                    currentUser, amount, new Category(categoryStr), "расход по CLI");
            return result;
        } catch (NumberFormatException e) {
            return "Ошибка: некорректная сумма.";
        }
    }

    public String addCategory(String categoryName, String limitStr) {
        if (!isUserLoggedIn()) return "Сначала авторизуйтесь.";

        try {
            BigDecimal limit = new BigDecimal(limitStr);
            Category category = new Category(categoryName);
            String result = config.getSetBudgetUseCase().setBudget(
                    currentUser, category, limit);
            return result;
        } catch (NumberFormatException e) {
            return "Ошибка: некорректная сумма лимита.";
        }
    }

    public String showSummary(String outputFile) {
        if (!isUserLoggedIn()) return "Сначала авторизуйтесь.";
        return config.getGetSummaryUseCase().getSummary(currentUser, outputFile);
    }

    public String showBudget(String outputFile) {
        if (!isUserLoggedIn()) return "Сначала авторизуйтесь.";
        return config.getGetBudgetStatusUseCase().getBudgetStatus(currentUser, outputFile);
    }

    public String showExpenses(List<String> categoryNames, String outputFile) {
        if (currentUser == null) {
            return "Ошибка: никто не залогинен.";
        }
        // Вызываем Use Case
        return config.getGetShowExpensesUseCase().execute(currentUser, categoryNames, outputFile);
    }

    public String transfer(String loginTo, String amountStr) {
        if (!isUserLoggedIn()) return "Сначала авторизуйтесь.";

        try {
            BigDecimal amount = new BigDecimal(amountStr);

            // Подгружаем получателя в память, если есть
            boolean loaded = config.getLoadUserDataUseCase().loadUserToMemory(loginTo);

            // Ищем его в InMemory
            var toUserOpt = config.getInMemoryRepository().findByLogin(loginTo);
            if (toUserOpt.isEmpty()) {
                return "Ошибка: пользователь " + loginTo + " не найден.";
            }
            var toUser = toUserOpt.get();

            String warning = config.getTransferFundsUseCase().transfer(currentUser, toUser, amount);
            if (warning.isEmpty()) {
                return "Перевод успешно выполнен.";
            } else {
                return warning; // Может вернуть предупреждение о минусовом балансе и т.п.
            }

        } catch (NumberFormatException e) {
            return "Ошибка: некорректная сумма.";
        }
    }

    // ---------------- Вспомогательные методы ----------------

    private boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUserLogin() {
        return currentUser != null ? currentUser.getLogin() : "";
    }
}
