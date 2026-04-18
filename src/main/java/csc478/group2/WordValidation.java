package csc478.group2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class WordValidation {

    private final Set<String> validWords = new HashSet<>();

    public WordValidation() {
        loadWords();
    }

    private void loadWords() {
        try (InputStream input = getClass().getResourceAsStream("/csc478/group2/sowpods.txt")) {
            if (input == null) {
                throw new IllegalStateException("Could not find sowpods.txt");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    validWords.add(line.trim().toUpperCase());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load SOWPODS", e);
        }
    }

    public boolean isValidWord(String word) {
        return validWords.contains(word.toUpperCase());
    }
}