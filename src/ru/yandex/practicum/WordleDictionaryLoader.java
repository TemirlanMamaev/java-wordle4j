package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final PrintWriter log;
    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }
    public WordleDictionary load(String fileName) throws IOException {
        log.println(fileName);
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        }

        log.println("Кол-во строк - " + words.size());

        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст.");
        }
        return new WordleDictionary(words);
    }

}
