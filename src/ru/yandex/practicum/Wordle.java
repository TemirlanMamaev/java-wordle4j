package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter("wordle.log", true))) {

            try {
                WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
                WordleDictionary dictionary = loader.load("words_ru.txt");
                WordleGame game = new WordleGame(log, dictionary);
                Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

                while (!game.isGameOver()) {
                    System.out.println("Осталось попыток: " + game.getSteps());
                    System.out.print("> ");

                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        String hintWord = game.getHintWord();

                        if (hintWord != null) {
                            System.out.println("Подсказка: " + hintWord);
                        } else {
                            System.out.println("Подходящих слов больше нет");
                        }

                    } else {
                        input = input.toLowerCase();
                        input = input.replace('ё', 'е');

                        try {
                            String hint = game.makeGuess(input);
                            System.out.println(hint);

                        } catch (WrongLengthException | WordNotFoundInDictionary e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                if (game.isWin()) {
                    System.out.println("Вы победили!");
                } else {
                    System.out.println("Попытки закончились.");
                }

                System.out.println("Загаданное слово: " + game.getAnswer());

            } catch (Exception e) {
                e.printStackTrace(log);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}