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

    public List<Article> searchArticleByAuthor (String queryAuthor){

        Map<Article, Double> similarityScores = new HashMap<>();
        for (Article article : articleList) {
            TextVector queryVector = new TextVector(queryAuthor);
            TextVector authorVector = new TextVector( article.getAuthor() );
            double similarity = calculateSimilarity(queryVector, authorVector);
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

    public List<Article> searchArticleByHashtag(String queryHashtag) {

        Map<Article, Double> similarityScores = new HashMap<>();
        for (Article article : articleList) {
            TextVector queryVector = new TextVector(queryHashtag);
            TextVector hashtagVector = new TextVector( article.getTags() );
            double similarity = calculateSimilarity(queryVector, hashtagVector);
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
        if ( id < articleList.size() ) {
            return articleList.get(id);
        }
        return null;
    }

    public List<Article> searchArticleUnionMultipleCriterions(List<Pair<String, String>> queries) {
        Set<Integer> setOfIds = new LinkedHashSet<>();
        Search search = new Search();
        List<Article> result = new ArrayList<>();
        for (Pair<String, String> query : queries) {
            List<Article> temp = new ArrayList<>();
            switch (query.getKey()){
                case "Title":
                    temp = search.searchArticleByTitle(query.getValue());
                    break;
                case "Author":
                    temp = search.searchArticleByAuthor(query.getValue());
                    break;
                case "Hashtag":
                    temp = search.searchArticleByHashtag(query.getValue());
                    break;
                case "ID":
                    temp.add(search.searchArticleByID(Integer.parseInt(query.getValue())));
                    break;
                default:
                    temp = search.getArticleList();
            }
            for (Article article : temp) {
                setOfIds.add(article.getId());
            }
        }
        for (Integer id : setOfIds) {
            result.add(searchArticleByID(id));
        }
        System.out.println();
        return result;
    }

    public List<Article> searchArticleIntersectMutipleCriterions(List<Pair<String, String>> queries) {
        Search search = new Search();
        Search tempSearch = new Search();
        for ( Pair<String, String> query : queries ) {

                switch (query.getKey()){
                    case "Title":
                        search.setArticleList(search.searchArticleByTitle(query.getValue()));
                        break;
                    case "Author":
                        search.setArticleList(search.searchArticleByAuthor(query.getValue()));
                        break;
                    case "Hashtag":
                        search.setArticleList(search.searchArticleByHashtag(query.getValue()));
                        break;
                    case "ID" :
                        List<Article> temp = new ArrayList<>();
                        for ( Article article : search.getArticleList() ) {
                            if ( article.getId() == Integer.parseInt(query.getValue()) ) {

                                if ( tempSearch.searchArticleByID(Integer.parseInt(query.getValue()) ) != null ) {
                                    temp.add(tempSearch.searchArticleByID(Integer.parseInt(query.getValue())));
                                }
                            }
                        }
                        search.setArticleList( temp );
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
