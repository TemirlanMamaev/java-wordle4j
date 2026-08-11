package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private final Set<String> usedHints = new HashSet<>();

    private final PrintWriter log;

    private String answer;

    private boolean win = false;

    public String getAnswer() {
        return answer;
    }

    private int steps;

    public int getSteps() {
        return steps;
    }

    private WordleDictionary dictionary;

    private List<String> varOfAnswer;

    public WordleGame(PrintWriter log, WordleDictionary dictionary) {
        this.log = log;
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 6;
        this.varOfAnswer = new ArrayList<>(dictionary.getWords());
    }

    public boolean isGameOver() {
        return steps <= 0 || win;
    }

    public boolean isWin() {
        return win;
    }

    public String makeGuess(String guess) throws WrongLengthException, WordNotFoundInDictionary {
        if (guess.length() != 5) {
            throw new WrongLengthException("Слово должно состоять из 5 букв.");
        }
        if (!dictionary.contains(guess)) {
            throw new WordNotFoundInDictionary("Это слово отсутствует в словаре или же оно состоит не из русских букв.");
        }
        String hint = WordleDictionary.checkWord(answer, guess);
        if (hint.equals("+++++")) {
            win = true;
            log.println("Игрок победил.");
        }

        List<String> filtered = new ArrayList<>();
        for (String candidate : varOfAnswer) {
            if (isCompatible(candidate, guess, hint)) {
                filtered.add(candidate);

            }

        }
        varOfAnswer = filtered;
        log.println("Загаданное слово" + answer);
        steps--;
        return hint;


    }

    private boolean isCompatible(String varOfAnswer, String guess, String hint) {
        String actualHint = WordleDictionary.checkWord(varOfAnswer, guess);
        return actualHint.equals(hint);
    }

    public String getHintWord() {

        List<String> availableHints = new ArrayList<>();

        for (String word : varOfAnswer) {
            if (!usedHints.contains(word)) {
                availableHints.add(word);
            }
        }

        if (availableHints.isEmpty()) {
            return null;
        }

        Random random = new Random();

        String hintWord =
                availableHints.get(random.nextInt(availableHints.size()));

        usedHints.add(hintWord);

        return hintWord;
    }

}
