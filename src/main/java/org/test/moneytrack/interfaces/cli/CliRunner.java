package org.test.moneytrack.interfaces.cli;

import org.test.moneytrack.config.ApplicationConfig;
import org.test.moneytrack.interfaces.controller.MoneyTrackController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CliRunner {

    private final MoneyTrackController controller;

    public CliRunner(ApplicationConfig config) {
        // Создаём контроллер
        this.controller = new MoneyTrackController(config);
    }

    private String extractOutputFile(String[] tokens) {
        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i].startsWith("--output=")) {
                return tokens[i].substring("--output=".length());
            }
        }
        return null;
    }

    public void startCliLoop() {
        System.out.println("Добро пожаловать в MoneyTrack CLI! Введите 'help' для списка команд.");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                String[] tokens = input.split("\\s+");
                String command = tokens[0].toLowerCase();
                String result;

                switch (command) {
                    case "help" -> {
                        result = """
                                Доступные команды:
                                  register {login} {password}
                                  login {login} {password}
                                  whoami
                                  logout
                                  add_income {amount} [category]
                                  add_expense {amount} {category}
                                  set_category {category} {budget}
                                  show_summary [--output=<filename>]
                                  show_budget [--output=<filename>]
                                  show_expenses {cat1} {cat2} ... [--output=<filename>]
                                  transfer {loginTo} {amount}
                                  exit
                                """;
                        System.out.println(result);
                    }
                    case "register" -> {
                        if (tokens.length < 3) {
                            System.out.println("Использование: register {login} {password}");
                            continue;
                        }
                        result = controller.register(tokens[1], tokens[2]);
                        System.out.println(result);
                    }
                    case "login" -> {
                        if (tokens.length < 3) {
                            System.out.println("Использование: login {login} {password}");
                            continue;
                        }
                        result = controller.login(tokens[1], tokens[2]);
                        System.out.println(result);
                    }
                    case "whoami" -> {
                        System.out.println(controller.getCurrentUserLogin());
                    }
                    case "logout" -> {
                        result = controller.logout();
                        System.out.println(result);
                    }
                    case "add_income" -> {
                        String amount = (tokens.length > 1) ? tokens[1] : "";
                        String category = (tokens.length > 2) ? tokens[2] : null;
                        result = controller.addIncome(amount, category);
                        System.out.println(result);
                    }
                    case "add_expense" -> {
                        if (tokens.length < 3) {
                            System.out.println("Использование: add_expense {amount} {category}");
                            continue;
                        }
                        result = controller.addExpense(tokens[1], tokens[2]);
                        System.out.println(result);
                    }
                    case "set_category" -> {
                        if (tokens.length < 3) {
                            System.out.println("Использование: set_category {category} {budget}");
                            continue;
                        }
                        result = controller.addCategory(tokens[1], tokens[2]);
                        System.out.println(result);
                    }
                    case "transfer" -> {
                        if (tokens.length < 3) {
                            System.out.println("Использование: transfer {loginTo} {amount}");
                            continue;
                        }
                        result = controller.transfer(tokens[1], tokens[2]);
                        System.out.println(result);
                    }

                    case "show_summary" -> {
                        String outputFile = extractOutputFile(tokens);
                        result = controller.showSummary(outputFile);
                        System.out.println(result);
                    }

                    case "show_budget" -> {
                        String outputFile = extractOutputFile(tokens);
                        result = controller.showBudget(outputFile);
                        System.out.println(result);
                    }

                    case "show_expenses" -> {
                        if (tokens.length < 2) {
                            System.out.println("Использование: show_expenses {cat1} {cat2} ... [--output=filename]");
                            break;
                        }
                        String outputFile = null;
                        List<String> categoryNames = new ArrayList<>();

                        // Пройдём по всем аргументам, начиная со второго
                        for (int i = 1; i < tokens.length; i++) {
                            if (tokens[i].startsWith("--output=")) {
                                outputFile = tokens[i].substring("--output=".length());
                            } else {
                                // название категории
                                categoryNames.add(tokens[i]);
                            }
                        }
                        result = controller.showExpenses(categoryNames, outputFile);
                        System.out.println(result);
                    }

                    case "exit" -> {
                        if (!Objects.equals(controller.getCurrentUserLogin(), "")) {
                            result = controller.logout();
                            System.out.println(result);
                        }
                        System.out.println("Завершение работы...");
                        running = false;
                    }
                    default -> System.out.println("Неизвестная команда. 'help' для списка команд.");
                }
            }
        }
    }
}
