package csc478.group2;

import java.util.HashSet;
import java.util.Set;

public class WordValidation {

    private Set<String> validWords;

    public WordValidation() {
        validWords = new HashSet<>();
        loadWords();
    }
    
    //Cost money or is copyrighted to use scrabble disctionary.. not gonna lie i assumed worcs were open source
    //need to find word list that uses similar amount of words
    private void loadWords() {
        validWords.add("CAT");
        validWords.add("DOG");
        validWords.add("READ");
        validWords.add("WORD");
        validWords.add("HELLO");
        validWords.add("JAVA");
        validWords.add("CODE");
        validWords.add("GAME");
    }

    public boolean isValidWord(String word) {
        return validWords.contains(word.toUpperCase());
    }
}