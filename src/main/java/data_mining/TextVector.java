package data_mining;

import java.util.HashMap;
import java.util.Map;

public class TextVector {
    private final Map<String, Integer> vector;

    public TextVector(String text) {
        this.vector = vectorizeText(text);
    }

    private Map<String, Integer> vectorizeText(String text) {
        Map<String, Integer> vector = new HashMap<>();
        String[] words = text.split("\\s+");

        for (String word : words) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }

        return vector;
    }

    public Map<String, Integer> getVector() {
        return vector;
    }
}

