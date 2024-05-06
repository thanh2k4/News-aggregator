package data.mining;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class Search {

    private final static double THRESHOLD = 0.75;

    public static double calculateSimilarity(TextVector queryVector, TextVector titleVector) {
        Map<String, Integer> query = queryVector.getVector();
        Map<String, Integer> title = titleVector.getVector();
        Map<String , Integer > similarityVector = new HashMap<>();
        int numberOfWordsInTitle = 0;
        int countSimilarity = 0;
        for ( Map.Entry<String, Integer> entryQuery : query.entrySet()) {
            for ( Map.Entry<String, Integer> entryTitle : title.entrySet()) {
                if ( 1 - LevenshteinDistance.calculateDistance( entryTitle.getKey() , entryQuery.getKey())*1.0 / entryTitle.getKey().length() >= THRESHOLD ){
                    similarityVector.put(entryTitle.getKey(), similarityVector.getOrDefault(entryTitle.getKey(),0) + 1);
                }
            }
        }

        for ( Map.Entry<String, Integer> entryTitle : title.entrySet()) {
            numberOfWordsInTitle += entryTitle.getValue();
            countSimilarity += Math.min(similarityVector.getOrDefault(entryTitle.getKey() , 0) , entryTitle.getValue());
        }

        return countSimilarity*1.0/numberOfWordsInTitle;
    }

    public static List<Map.Entry<Aggreator, Double>> sortedAggreator(String query) {
        Gson gson = new Gson();
        List<Aggreator> aggreatorList = null;
        try (FileReader reader = new FileReader("src/main/resources/data.json")) {
            Aggreator[] aggreatorArray = gson.fromJson(reader, Aggreator[].class);
            aggreatorList = Arrays.asList(aggreatorArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Aggreator, Double> similarityScores = new HashMap<>();
        for (Aggreator aggregator : aggreatorList) {
            TextVector queryVector = new TextVector(query);
            TextVector titleVector = new TextVector(aggregator.getTitle());
            double similarity = calculateSimilarity(queryVector, titleVector);
            similarityScores.put(aggregator, similarity);
        }

        List<Map.Entry<Aggreator, Double>> sortedResults = new ArrayList<>(similarityScores.entrySet());
        sortedResults.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        return sortedResults;
    }

}
