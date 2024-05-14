package history;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import crawler.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistorySearchFileManager {
    private static final String HISTORY_FILE_PATH = "History.json";
    private static ObservableList<HistorySearchModel> historySearchModels = FXCollections.observableArrayList();

    public void loadJson() {
        try (FileReader reader = new FileReader(HISTORY_FILE_PATH)) {
            Gson gson = new Gson();
            Type historySearchModelListType = new TypeToken<ArrayList<HistorySearchModel>>(){}.getType();
            ArrayList<HistorySearchModel> data = gson.fromJson(reader, historySearchModelListType);
            if (data != null) {
                historySearchModels.clear();
                historySearchModels.addAll(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJson(Article article) {
        loadJson();
        try (FileWriter fileWriter = new FileWriter(HISTORY_FILE_PATH)) {
            HistorySearchModel model = new HistorySearchModel(article.getId(), article.getLink(), article.getTitle());
            historySearchModels.add(0, model);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(historySearchModels, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<HistorySearchModel> getHistorySearchModels() {
        return historySearchModels;
    }
}
