package data_mining;

import crawler.Article;

import java.time.LocalDate;
import java.util.*;



public class Search {

    private final static double THRESHOLD = 0.7;

    private List<Article> articleList;

    Search(){
        this.articleList = LoadArticle.getArticleList();
    }

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

    public List<Article> searchArticleByTitle(String query) {

        Map<Article, Double> similarityScores = new HashMap<>();
        for (Article article : articleList) {
            TextVector queryVector = new TextVector(query);
            TextVector titleVector = new TextVector( article.getTitle() );
            double similarity = calculateSimilarity(queryVector, titleVector);
            similarityScores.put(article, similarity);
        }

        List<Map.Entry<Article, Double>> sortedArticles = new ArrayList<>(similarityScores.entrySet());
        sortedArticles.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        List<Article> result = new ArrayList<>();

        for ( Map.Entry<Article, Double> entry : sortedArticles ){
            result.add( entry.getKey() );
        }

        return result;
    }

    public List<Article> searchArticleByAuthor (String author){
        
    }

    public List<Article> searchArticleByAll(String queryAll) {
    }

    public List<Article> searchArticleByContent(String queryContent) {
    }

    public List<Article> searchArticleByHashtag(String queryHashtag) {

    }

    public List<Article> searchArticleByDate(LocalDate startDate, LocalDate endDate) {
        List<Article> result = new ArrayList<>();

        for (Article article : articleList) {
            if (article.getDate().isAfter(startDate) && article.getDate().isBefore(endDate)) {
                result.add(article);
            }
        }

        return result;
    }
}
