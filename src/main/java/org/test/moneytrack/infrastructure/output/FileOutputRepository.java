package org.test.moneytrack.infrastructure.output;

import org.test.moneytrack.application.port.OutputRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Реализация OutputRepository,
 * которая физически записывает строки в файловую систему.
 */
public class FileOutputRepository implements OutputRepository {

    @Override
    public void writeToFile(String directory, String filename, String content) {
        try {
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Files.writeString(dirPath.resolve(filename), content);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл " + filename + ": " + e.getMessage());
        }
    }
}
