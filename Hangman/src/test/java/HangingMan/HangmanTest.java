package HangingMan;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class HangmanTest {
    public static final String nameSaveFile = "src/test/resources/SavedGame.txt";
    public static final String nameHighScores = "src/test/resources/HighScores.txt";
    public static final String nameHeterograms = "src/test/resources/Heterograms.txt";
    public static Hangman hangman;
    public static String name = "Me";

    @BeforeEach     // Create new hangman object
    void setup() {
        Scanner scan = new Scanner(System.in);
        hangman = new Hangman(scan, name);
    }

    @AfterEach
    void teardown() {
        hangman.scan.close();
    }

    @Test
    void validValidateMenuOptionTest() {
        List<String> values = List.of("n", "new", "c", "continue", "s", "score", "h", "help", "q", "quit");
        for (String val : values) { assertTrue(hangman.validateMenuOption(val), "Menu selection not registering correctly"); }
    }

    @Test
    void invalidValidateMenuOptionTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String val : values) { assertFalse(hangman.validateMenuOption(val), "Invalid menu option being accepted"); }
    }

    @Test   // Input buffer
    void validInputMenuTest() {
        hangman.scan.close();
        List<String> values = List.of("n", "new", "c", "continue", "s", "score", "h", "help", "q", "quit");
        for (String val : values) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals(val, hangman.inputMenu(), "Input menu is NOT correctly accepting input");
            hangman.scan.close();
        }
    }

    @Test   // Input buffer
    void invalidInputMenuTest() {
        hangman.scan.close();
        List<String> values = List.of("Brian n", "Jordan n", "Jacob n", "Zach n", "Mike n", "Luke n", "sdfjf n", "SDlsucVCIU n",
                "JdfklsKSqwFlkFDlS n", "NwefS n", "SDfjoefm n", "xcVOPem n", ".,mwefoi n");
        for (String val : values) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals("n", hangman.inputMenu(), "Improperly handles incorrect input");
            hangman.scan.close();
        }
    }

    @Test
    void keywordTest() {
        List<String> invalid = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String val : invalid) {
            assertFalse(hangman.isKeyword(val, "", "", ""), "Invalid word accepted as keyword");
        }
        List<String> valid = List.of("help");//, "save", "quit"); // save and quit will exit system and should end test
        for (String val : valid) {
            hangman.scan = new Scanner(new ByteArrayInputStream("%n".getBytes()));
            assertTrue(hangman.isKeyword(val, "", "", ""), "Keyword not registering correctly");
        }
    }

    @Test
    void correctValidateLetterTest() {
        List<String> values = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        for (String letter : values) {
            assertTrue(hangman.validateLetter(letter, ""), String.format("%s is not registering as a letter", letter));
            assertTrue(hangman.validateLetter(letter.toLowerCase(), ""), String.format("%s is not registering as a letter", letter.toLowerCase()));
        }
    }

    @Test
    void incorrectValidateLetterTest() {
        String guessed = "abcdefghijklmnopqrstuvwxyz";
        List<String> invalid = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "*sdfjf", "%SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "-NwefS", "(SDfjoefm", "xcVOPem", ".,mwefoi", "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        for (String val : invalid) {
            assertFalse(hangman.validateLetter(val, guessed), String.format("%s is incorrectly being considered a valid letter", val));
            assertFalse(hangman.validateLetter(val.toLowerCase(), guessed), String.format("%s is incorrectly being considered a valid letter", val));
            assertFalse(hangman.validateLetter(val.toUpperCase(), guessed), String.format("%s is incorrectly being considered a valid letter", val));
        }
    }

    @Test
    void validCheckLetterTest() {
        String secret = "abcdefghijklmnopqrstuvwxyz";
        List<String> values = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", " ");
        hangman.gallows.reset();
        for (String letter : values) {
            char l = letter.charAt(0);
            String[] strings = {secret, "", " ", null};
            for (String str : strings) {
                assertEquals("", hangman.checkLetterContained(l, str, ""), String.format("%s should not be added to incorrect letters", letter));
                assertEquals(Gallows.initialChances, hangman.gallows.getChancesRemaining(), "Lives decreased when it shouldn't have");
            }
        }
    }

    @Test
    void invalidCheckLetterTest() {
        List<String> values = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        for (String letter : values) {
            hangman.gallows.reset();
            assertEquals(letter, hangman.checkLetterContained(letter.charAt(0), "123", ""), String.format("%s isn't being added to incorrectGuesses", letter));
            assertEquals(Gallows.initialChances-1, hangman.gallows.getChancesRemaining(), "Lives are not decreasing properly");
        }
    }

    @Test
    void hiddenWordTest() {
        String guessed = "abcdefghijklmnopqrstuvwxyz";
        List<String> words = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "*sdfjf", "%SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "-NwefS", "(SDfjoefm", "xcVOPem", ".,mwefoi", "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        for (String word : words) {
            String w = word.replaceAll("[^a-zA-Z0-9]", "");
            assertEquals("_".repeat(w.length()), hangman.getHiddenWord(word, ""), "Word is not properly hidden");
            assertEquals(w, hangman.getHiddenWord(word, guessed), "Word is not properly uncovered");
        }
    }

    @Test
    void hiddenCharCountTest() {
        String secret = "abcdefghijklmnopqrstuvwxyz";
        List<String> guesses = hangman.loadWordBuffer("a", nameHeterograms);
        for (String revealed : guesses) {
            assertEquals(26-revealed.length(), hangman.getHiddenCharCount(secret, revealed),
                    String.format("The number of hidden chars in secret word %s is incorrect", revealed));
            assertEquals(26-revealed.length(), hangman.getHiddenCharCount(hangman.getHiddenWord(secret, revealed)),
                    String.format("The number of hidden chars in hidden word %s is incorrect", revealed));
        }
    }

    @Test
    void revealedCharCountTest() {
        String secret = "abcdefghijklmnopqrstuvwxyz";
        List<String> guesses = hangman.loadWordBuffer("a", nameHeterograms);
        for (String revealed : guesses) {
            assertEquals(revealed.length(), hangman.getRevealedCharCount(secret, revealed), "The number of revealed chars in secret word is incorrect");
            assertEquals(revealed.length(), hangman.getRevealedCharCount(hangman.getHiddenWord(secret, revealed)),
                    "The number of revealed chars in hidden word is incorrect");
        }
    }




    @Test   // Input buffer, secret, letters guessed, chances
    void gameplayLoopFailTest() {
        hangman.scan.close();
        List<String> fails = Arrays.asList("h i j k l m n", "a b c d e f g n"); //, "quit"); // quit will exit system and should end test
        for (String val : fails) {
            hangman.gallows.reset();
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertFalse(hangman.gameplayLoop("abcdefg", "", ""), "Gameplay loop incorrectly returning TRUE");
            hangman.scan.close();
        }
    }

    @Test
    void gameplayLoopSuccessTest() {
        hangman.scan.close();
        List<String> success = Arrays.asList("h i j k l m y", "a b c d e f g y");
        for (String val : success) {
            hangman.gallows.reset();
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertTrue(hangman.gameplayLoop("abcdefg", "", ""), "Gameplay loop incorrectly returning FALSE");
            hangman.scan.close();
        }
    }



    @Test
    void validPlayAgainTest() {
        List<String> valid = List.of("y", "yes", "n", "no");
        for (String response : valid) {
            assertTrue(hangman.validPlayAgain(response), "Valid responses are claimed invalid");;
        }
        List<String> invalid = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (String response : invalid) {
            assertFalse(hangman.validPlayAgain(response), "Invalid responses are being considered valid");
        }
    }

    @Test   // Set Player name, file location, secret word, letters guessed and incorrect
    void saveGameTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        String guessed = "abcdefg";
        String incorrect = "hijklmno";
        for (int i = 0; i < values.size(); i++) {
            Hangman h = new Hangman(hangman.scan, "Brian" + i);
            assertTrue(h.saveGame(nameSaveFile, values.get(i), guessed, incorrect), "Game not saving correctly");
        }
    }

    @Test   // Set player name and file location
    void loadGameTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        String guessed = "abcdefg";
        String incorrect = "hijklmno";
        for (int i = 0; i < values.size(); i++) {
            Hangman h = new Hangman(hangman.scan, "Brian" + i);
            h.saveGame(nameSaveFile, values.get(i), guessed, incorrect);
            List<String> load = h.loadGame(nameSaveFile);
            assertEquals(values.get(i), load.get(0), "Secret word failed to load");
            assertEquals(guessed, load.get(1), "failed to load letters guessed");
            assertEquals(incorrect, load.get(2), "failed to load incorrect letters");
        }
    }

    @Test
    void validDifficultyTest() {
        List<String> values = List.of("a", "any", "e", "easy", "n", "normal", "h", "hard", "c", "challenging");
        for (String response : values) {
            assertTrue(hangman.validDifficulty(response), "Difficulty is not being registered");
        }
        List<String> invalid = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "*sdfjf", "%SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "-NwefS", "(SDfjoefm", "xcVOPem", ".,mwefoi", "");
        for (String response : invalid) {
            assertFalse(hangman.validDifficulty(response), "Invalid difficulty is being accepted");
        }
    }

    @Test   // Pass an empty list and a filled list, player and calculate score
    void addScoreToListTest() {
        for (int i = -200; i < 600; i += 10) {
            ArrayList<String> list = new ArrayList<>();
            hangman.addScoreToList(list, i);
            assertEquals(name, list.get(0), "Name not being added to score list");
            assertEquals(Integer.toString(i), list.get(1), "Score not being added to score list");
        }
        ArrayList<String> list = null;
        hangman.addScoreToList(list, 200);
        assertNull(list, "Null list should remain null but isn't");
    }

    @Test   // Fill sheet with excess values
    void reduceScoresSheetTest() {
        for (int size = 0; size < 100; size++) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) list.add(Integer.toString(i));
            Hangman.reduceScoresSheet(list);
            assertTrue((3 * Hangman.scoreSheetSize) >= list.size(), "Reduce failing to shrink array size");
            System.out.printf("Starting size of score: %s%nFinal size of score: %s%n", size, list.size());
        }
    }

    @Test   // Set file location, player, and calculate score
    void updateScoresTest() {
        assertTrue(hangman.updateScores(nameHighScores, 100), "Scores are not updating properly");
        assertFalse(hangman.updateScores(null, 100), "Invalid score claiming to update");
    }

    @Test
    void dictionaryTest() {
        hangman.scan.close();
        List<String> values = List.of("e", "easy", "n", "normal", "h", "hard", "c", "challenging", "a", "any");
        for (String diff : values) {
            for (int i = 0; i < 20; i++) {
                hangman.scan = new Scanner(new ByteArrayInputStream(diff.getBytes()));
                // load the dictionary based on difficulty then pick a random word
                hangman.inputLoadWordByDifficulty();
                String word = hangman.wordBuffer.get(ThreadLocalRandom.current().nextInt(0, 99));
                // the length of the word should be between getwordmin and getwordmax
                assertTrue(word.length() > Hangman.getWordMin(diff) && word.length() < Hangman.getWordMax(diff));
            }
         }
    }

    @Test
    void testEncodingProcess() {
        List<String> values = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "n", "new", "c", "continue", "s", "score", "h",
                "save", "help", "y", "yes", "n", "no", "q", "quit", "Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke",
                "sdfjf", "SDlsucVCIU", "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (String val : values) {
            // ensure encoding then decoding returns the original word
            assertEquals(val, Hangman.decodeString(Hangman.encodeString(val)), "Error with encoding");
        }
    }
}