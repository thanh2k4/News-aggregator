package data_mining;

import crawler.Article;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;



public class Search {

    private final static double THRESHOLD = 0.7;

    private List<Article> articleList;

    public Search(){
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
        List<Article> result = new ArrayList<>();
        for ( Article article : articleList ){
            if ( article.getAuthor().equals(author) ){
                result.add( article );
            }
        }
        return result;
    }

    public List<Article> searchArticleByHashtag(String queryHashtag) {
        return null;
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

    public Article searchArticleByID(int id) {
        return articleList.get(id);
    }

    public List<Article> searchArticleUnionMultipleCriterions(List<Pair<String, String>> queries) {
        Set<Integer> setOfIds = new HashSet<>();
        Search search = new Search();
        List<Article> result = new ArrayList<>();
        for (Pair<String, String> query : queries) {
            List<Article> temp = new ArrayList<>();
            switch (query.getKey()){
                case "title":
                    temp = search.searchArticleByTitle(query.getValue());
                    break;
                case "author":
                    temp = search.searchArticleByAuthor(query.getValue());
                    break;
                case "hashtag":
                    temp = search.searchArticleByHashtag(query.getValue());
                    break;
                case "id":
                    temp.add(search.searchArticleByID(Integer.parseInt(query.getValue())));
            }
            for (Article article : temp) {
                setOfIds.add(article.getId());
            }
        }
        for (Integer id : setOfIds) {
            result.add(searchArticleByID(id));
        }
        return result;
    }

    public List<Article> searchArticleIntersectMutipleCriterions(List<Pair<String, String>> queries) {
        Search search = new Search();
        for ( Pair<String, String> query : queries ) {
            switch (query.getKey()){
                case "title":
                    search.setArticleList(search.searchArticleByTitle(query.getValue()));
                    break;
                case "author":
                    search.setArticleList(search.searchArticleByAuthor(query.getValue()));
                    break;
                case "hashtag":
                    search.setArticleList(search.searchArticleByHashtag(query.getValue()));
                    break;
                case "id" :
                    search.setArticleList(new ArrayList<>(Collections.singletonList(search.searchArticleByID(Integer.parseInt(query.getValue())))));
                    break;
            }
        }
        return search.getArticleList();
    }
    public List<Article> getArticleList() {
        return articleList;
    }
    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
