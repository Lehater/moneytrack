package org.test.moneytrack.application.port;

/**
 * Абстракция вывода информации в файл (или другой внешний ресурс).
 */
public interface OutputRepository {

    /**
     * Записывает содержимое `content` в указанный `filename`.
     */
    void writeToFile(String directory,String filename, String content);
}
