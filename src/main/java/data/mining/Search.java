package data.mining;

import com.google.gson.Gson;
import edu.stanford.nlp.simple.*;
import java.io.FileReader;
import java.io.IOException;
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
        System.out.println(queryTokens);
        for (Sentence sent : doc2.sentences()) {
            titleTokens.addAll(sent.words());
        }
        System.out.println(titleTokens);

        // Use Jaccard to caculate the similarity
        Set<String> intersection = new HashSet<>(queryTokens);
        intersection.retainAll(titleTokens);
        double intersectionSize = intersection.size();
        System.out.println("intersectionSize: " + intersectionSize);
        double unionSize = queryTokens.size() + titleTokens.size() - intersectionSize;
        System.out.println("Union size: " + unionSize);
        return intersectionSize / unionSize;
    }

    public static List<String> tokenize(String text) {
        // Implement your tokenization logic here
        return Arrays.asList(text.split("\\s+"));
    }

    public static void main(String[] args) {
        //Take data from JSON file
        Gson gson = new Gson();
        List<Aggreator> aggreatorList = null;
        try (FileReader reader = new FileReader("C://Users/ADMIN/IdeaProjects/News-aggregator/src/main/java/data/mining/data.json")) {
            Aggreator[] aggreatorArray = gson.fromJson(reader, Aggreator[].class);
            aggreatorList = Arrays.asList(aggreatorArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the title you want to search for from GUI
        String query = "block";

        // Data preprocessing and calculate the similarity
            Map<Aggreator, Double> similarityScores = new HashMap<>();
        for (Aggreator aggreator  : aggreatorList) {
            double similarity = calculateSimilarity(query, aggreator.getTitle());
            similarityScores.put( aggreator , similarity);
        }

        // Sort result
        List<Map.Entry<Aggreator, Double>> sortedResults = new ArrayList<>(similarityScores.entrySet());
        sortedResults.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // Give sortedResults for GUI
        int index = 0;
//        for( Map.Entry<Aggreator,Double> entry : sortedResults){
//            System.out.println("Title " + index +  " : " + entry.getKey().getTitle() + " : " + entry.getValue());
//            index++;
//        }
    }
}