package org.test.moneytrack;

import org.test.moneytrack.config.ApplicationConfig;
import org.test.moneytrack.interfaces.cli.CliRunner;

/**
 * Точка входа (main) для приложения "MoneyTrack".
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Запуск MoneyTrack...");

        // Создаём конфигурацию приложения
        ApplicationConfig config = new ApplicationConfig();

        // Передаём конфигурацию в CLI Runner
        CliRunner cli = new CliRunner(config);

        // Запускаем цикл чтения команд
        cli.startCliLoop();
    }
}
