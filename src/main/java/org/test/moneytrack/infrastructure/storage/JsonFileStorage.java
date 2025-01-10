package org.test.moneytrack.infrastructure.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.test.moneytrack.domain.model.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

/**
 * Вспомогательный класс для сохранения и загрузки объекта User в/из JSON-файла.
 * Использует Jackson.
 */
public class JsonFileStorage {

    private final ObjectMapper objectMapper;
    private final String dataDirectory; // Папка, где храним файлы JSON

    /**
     * @param dataDirectory Путь к директории, где будут лежать файлы JSON
     */
    public JsonFileStorage(String dataDirectory) {
        this.objectMapper = new ObjectMapper();

        // Регистрируем поддержку Java 8 Time API
        this.objectMapper.registerModule(new JavaTimeModule());
        // Чтобы даты писались в человеко-читаемом формате, а не как таймстэмпы:
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Включаем функцию "pretty print"
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.dataDirectory = dataDirectory;

        // Создадим директорию, если её нет
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Сохраняет пользователя в JSON-файл.
     * Файл будет называться data_{login}.json
     */
    public void saveUser(User user) throws IOException {
        String fileName = "data_" + user.getLogin() + ".json";
        File file = new File(dataDirectory, fileName);
        objectMapper.writeValue(file, user);
    }

    /**
     * Загружает пользователя (User) из JSON-файла по логину.
     * Если файл не найден или данные некорректны — возвращаем null.
     */
    public User loadUser(String login) {
        String fileName = "data_" + login + ".json";
        File file = new File(dataDirectory, fileName);
        if (!file.exists()) {
            return null; // Нет такого файла - пользователь не найден
        }
        try {
            return objectMapper.readValue(file, User.class);
        } catch (IOException e) {
            // Логируем ошибку (упрощённо)
            System.err.println("Ошибка чтения файла: " + file.getName());
            return null;
        }
    }

    /**
     * Удаляет файл, соответствующий пользователю (при необходимости).
     */
    public void deleteUser(String login) {
        String fileName = "data_" + login + ".json";
        File file = new File(dataDirectory, fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
