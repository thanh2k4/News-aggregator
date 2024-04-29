package data.mining;

import edu.stanford.nlp.simple.*;

import java.util.*;

public class Search {
    public static double calculateSimilarity(String query, String title) {
        // Use lib spaCy to data preprocessing
        Document doc1 = new Document(query);
        Document doc2 = new Document(title);

        // Get words from preprocessed text
        List<String> queryTokens = new ArrayList<>();
        List<String> titleTokens = new ArrayList<>();
        for (Sentence sent : doc1.sentences()) {
            queryTokens.addAll(sent.words());
        }
        for (Sentence sent : doc2.sentences()) {
            titleTokens.addAll(sent.words());
        }

        // Use Jaccard to caculate the similarity
        Set<String> intersection = new HashSet<>(queryTokens);
        intersection.retainAll(titleTokens);
        double intersectionSize = intersection.size();
        double unionSize = queryTokens.size() + titleTokens.size() - intersectionSize;
        return intersectionSize / unionSize;
    }

    public static void main(String[] args) {
        //Take data from JSON file
        List<String> dataset = Arrays.asList("Titlle1", "Titlle2", "Titlle3", "Titlle4", "Titlle5");

        //Get the title you want to search for from GUI
        String query = "Title";

        // Data preprocessing and calculate the similarity
        Map<String, Double> similarityScores = new HashMap<>();
        for (String title : dataset) {
            double similarity = calculateSimilarity(query, title);
            similarityScores.put(title, similarity);
        }

        // Sort result
        List<Map.Entry<String, Double>> sortedResults = new ArrayList<>(similarityScores.entrySet());
        sortedResults.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // Show result
        // Give sortedResults for GUI
    }
}