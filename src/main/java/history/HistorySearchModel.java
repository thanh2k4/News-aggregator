package history;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorySearchModel {
    private int id;
    private String webName;
    private String title;
    private String timestamp;

    public HistorySearchModel(int id, String webName, String title) {
        this.id = id;
        this.webName = webName;
        this.title = title;
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss");
        this.timestamp = localDateTime.format(formatter);
    }

    public String getWebName() {
		return webName;
	}
	public void setId(int id) {
		this.id = id;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
}
