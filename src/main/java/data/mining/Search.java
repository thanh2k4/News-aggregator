package data.mining;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class Search {

    private final static double THRESHOLD = 0.75;

    public static double calculateSimilarity(String query, String title) {
        Map<String, Integer > queryVector = vectorizeText(query);
        Map<String, Integer > titleVector = vectorizeText(title);
        Map<String , Integer > similarityVector = new HashMap<>();
        int numberOfWordsInTitle = 0;
        int countSimilarity = 0;
        for ( Map.Entry<String, Integer> entryQuery : queryVector.entrySet()) {
            for ( Map.Entry<String, Integer> entryTitle : titleVector.entrySet()) {
                if ( 1 - LevenshteinDistance.calculateDistance( entryTitle.getKey() , entryQuery.getKey())*1.0 / entryTitle.getKey().length() >= THRESHOLD ){
                    similarityVector.put(entryTitle.getKey(), similarityVector.getOrDefault(entryTitle.getKey(),0) + 1);
                }
            }
        }

        for ( Map.Entry<String, Integer> entryTitle : titleVector.entrySet()) {
            numberOfWordsInTitle += entryTitle.getValue();
            countSimilarity += Math.min(similarityVector.getOrDefault(entryTitle.getKey() , 0) , entryTitle.getValue());
        }

        return countSimilarity*1.0/numberOfWordsInTitle;
    }


    public static Map<String, Integer> vectorizeText(String text) {
        Map<String, Integer> vector = new HashMap<>();
        String[] words = text.split("\\s+");

        for (String word : words) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }

        return vector;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Aggreator> aggreatorList = null;
        try (FileReader reader = new FileReader("src/main/resources/data.json")) {
            Aggreator[] aggreatorArray = gson.fromJson(reader, Aggreator[].class);
            aggreatorList = Arrays.asList(aggreatorArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String query = "Blockchain";
        Map<Aggreator, Double> similarityScores = new HashMap<>();
        for (Aggreator aggreator : aggreatorList) {
            double similarity = calculateSimilarity(aggreator.getTitle() , query );
            similarityScores.put(aggreator, similarity);
        }

        List<Map.Entry<Aggreator, Double>> sortedResults = new ArrayList<>(similarityScores.entrySet());
        sortedResults.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        for (Map.Entry<Aggreator, Double> entry : sortedResults) {
            System.out.println("Tiêu đề: " + entry.getKey().getTitle() + ", Điểm tương đồng: " + entry.getValue());
        }
    }
}
