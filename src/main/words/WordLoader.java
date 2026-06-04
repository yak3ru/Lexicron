package main.words;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordLoader {

    private static final Random random = new Random();
    private static ArrayList<String> easyWords = new ArrayList<>();
    private static ArrayList<String> codingWords = new ArrayList<>();

    // file loader
    private static ArrayList<String> load(String path) {
        ArrayList<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    words.add(line.trim());
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading: " + path);
        }

        return words;
    }

    // loads txt file
    public static void loadWords() {

            //easyWords = load("words.txt");
            //codingWords = load("syntaxes.txt");

            easyWords = load("src/main/words/textfiles/words.txt");
            codingWords = load("src/main/words/textfiles/syntaxes.txt");
    }

    // random word chooser
    private static String getRndmWord(List<String> words) {
        if (words.isEmpty()) {
            return "";
        }
        return words.get(random.nextInt(words.size()));
    }

    // word getter
    public static String getEasyWord() {
        return getRndmWord(easyWords);
    }
    public static String getCodingWord() {
        return getRndmWord(codingWords);
    }
}
