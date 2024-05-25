package history;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class HistorySearchModelTypeAdapter extends TypeAdapter<HistorySearchModel> {
    @Override
    public HistorySearchModel read(JsonReader reader) throws IOException {
        HistorySearchModel historySearchModel = new HistorySearchModel();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("webName")) {
                historySearchModel.setWebName(reader.nextString());
            } else if (name.equals("title")) {
                historySearchModel.setTitle(reader.nextString());
            } else if (name.equals("timestamp")) {
                historySearchModel.setTitle(reader.nextString());
            } else {
                reader.skipValue(); // Skip unknown elements
            }
        }
        reader.endObject();

        return historySearchModel;
    }

    @Override
    public void write(JsonWriter writer, HistorySearchModel value) throws IOException {
        writer.beginObject();
        writer.name("webName").value(value.getWebName());
        writer.name("title").value(value.getTitle());
        writer.name("timestamp").value(value.getTimestamp());
        writer.endObject();
    }
}
