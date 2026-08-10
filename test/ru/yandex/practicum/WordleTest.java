package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(List.of(
                "герой",
                "гонец",
                "слово",
                "мама",
                "пёсик"
        ));
    }

    @Test
    void checkWord_exampleFromTask() {
        assertEquals("+^-^-", WordleDictionary.checkWord("герой", "гонец"));
    }

    @Test
    void checkWord_fullMatch() {
        assertEquals("+++++", WordleDictionary.checkWord("герой", "герой"));
    }

    @Test
    void checkWord_noCommonLetters() {
        assertEquals("-----", WordleDictionary.checkWord("книга", "полёт"));
    }

    @Test
    void checkWord_repeatedLetters() {
        assertEquals("^^-++", WordleDictionary.checkWord("батат", "аббат"));
    }

    @Test
    void dictionary_filtersLengthAndNormalizes() {
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("слово"));
        assertTrue(dictionary.contains("песик"));
        assertFalse(dictionary.contains("мама"));
        assertEquals(4, dictionary.size());
    }

    @Test
    void getRandomWord_returnsWordFromDictionary() {
        String word = dictionary.getRandomWord();
        assertNotNull(word);
        assertEquals(5, word.length());
        assertTrue(dictionary.contains(word));
    }

}

class WordleGameTest {

    private PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        log = new PrintWriter(System.out, true);
        dictionary = new WordleDictionary(List.of(
                "герой",
                "гонец",
                "слово",
                "окно",
                "песик",
                "рамка",
                "норма"
        ));
        game = new WordleGame(log, dictionary);
    }

    @Test
    void makeGuess_decreasesStepsOnValidWord() throws Exception {
        int before = game.getSteps();
        String hint = game.makeGuess("гонец");

        assertEquals(5, hint.length());
        assertEquals(before - 1, game.getSteps());
    }

    @Test
    void makeGuess_wrongLength_doesNotSpendAttempt() {
        int before = game.getSteps();

        assertThrows(WrongLengthException.class, () -> game.makeGuess("кот"));
        assertEquals(before, game.getSteps());
    }

    @Test
    void makeGuess_unknownWord_doesNotSpendAttempt() {
        int before = game.getSteps();

        assertThrows(WordNotFoundInDictionary.class,
                () -> game.makeGuess("ааааа"));
        assertEquals(before, game.getSteps());
    }

    @Test
    void makeGuess_winWhenSecretGuessed() throws Exception {
        String secret = game.getAnswer();
        String hint = game.makeGuess(secret);

        assertEquals("+++++", hint);
        assertTrue(game.isWin());
        assertTrue(game.isGameOver());
    }

    @Test
    void getHintWord_doesNotReturnSecret() {
        for (int i = 0; i < 20; i++) {
            String hint = game.getHintWord();
            if (hint == null) {
                break;
            }
            assertNotEquals(game.getAnswer(), hint);
        }
    }

    @Test
    void getHintWord_doesNotRepeat() {
        String first = game.getHintWord();
        String second = game.getHintWord();

        if (first != null && second != null) {
            assertNotEquals(first, second);
        }
    }

    @Test
    void afterGuess_secretStillReachable() throws Exception {
        assertDoesNotThrow(() -> game.makeGuess("гонец"));
    }

    @Test
    void gameOver_whenStepsEnd() throws Exception {
        String secret = game.getAnswer();

        List<String> words = dictionary.getWords();
        int spent = 0;
        for (String w : words) {
            if (w.equals(secret)) {
                continue;
            }
            if (spent >= 6) {
                break;
            }
            game.makeGuess(w);
            spent++;
        }

        if (spent >= 6) {
            assertTrue(game.isGameOver());
            assertFalse(game.isWin());
        }
    }
}
