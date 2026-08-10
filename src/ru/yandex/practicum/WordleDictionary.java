package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        List<String> goodWords = new ArrayList<>();
        for (String word : words) {
            String normalized = normalize(word);
            if (normalized.length() == 5) {
                goodWords.add(normalized);
            }
        }
        this.words = goodWords;
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public int size() {
        return words.size();
    }

    public String getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    private String normalize(String word) {
        word = word.toLowerCase();
        word = word.replace("ё", "е");
        return word;
    }

    public static String checkWord(String answer, String guess) {
        char[] copiedAnswer = answer.toCharArray();
        char[] finalWord = new char[5];
        for (int i = 0; i < 5; i++) {
            if (copiedAnswer[i] == guess.charAt(i)) {
                finalWord[i] = '+';
                copiedAnswer[i] = 'X';
            }

        }

        for (int i = 0; i < 5; i++) {
            if (finalWord[i] == '+') {
                continue;
            }

            boolean found = false;

            for (int j = 0; j < 5; j++) {
                if (copiedAnswer[j] == guess.charAt(i)) {
                    finalWord[i] = '^';
                    copiedAnswer[j] = 'X';
                    found = true;
                    break;
                }
            }

            if (!found) {
                finalWord[i] = '-';
            }
        }
        return new String(finalWord);
    }

}
