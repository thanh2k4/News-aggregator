package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crawler.Article;
import data_mining.LoadArticle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class StatisticController {
	@FXML
	private Button btnWebName;
	@FXML
	private Button btnTime;
	@FXML
	private PieChart pieChart;
	@FXML
    public void initialize() {
        btnWebName.setOnAction(event -> loadChartDataByWebName());
        btnTime.setOnAction(event -> loadChartDataByTime());
    }
    List<Article>articles = LoadArticle.getArticleList();
	public void loadChartDataByWebName() {
	    HashMap<String, Integer> webNameCounts = new HashMap<>();
	    for (Article article : articles) {
	        String webName = article.getSource();
	        if (webNameCounts.containsKey(webName)) {
	            webNameCounts.put(webName, webNameCounts.get(webName) + 1);
	        } else {
	            webNameCounts.put(webName, 1);
	        }
	    }
	    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	    for (Map.Entry<String, Integer> entry : webNameCounts.entrySet()) {
	        PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
	        data.nodeProperty().addListener(new ChangeListener<Node>() {
	            @Override
	            public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
	                if (newNode != null) {
	                    Tooltip tooltip = new Tooltip("Numbers of articles: " + (int)data.getPieValue());
	                    Tooltip.install(newNode, tooltip);
	                }
	            }
	        });
	        pieChartData.add(data);
	    }
	    pieChart.setData(pieChartData);
	}
	private void loadChartDataByTime() {
	    HashMap<String, Integer> monthCounts = new HashMap<>();
	    for (Article article : articles) {
	        if (article.getDate().equalsIgnoreCase("Unknown")) {
	            continue;
	        }
	        LocalDateTime dateTime = LocalDateTime.parse(article.getDate(), DateTimeFormatter.ISO_DATE_TIME);
	        String yearMonth = dateTime.getYear() + "-" + dateTime.getMonthValue();

	        if (monthCounts.containsKey(yearMonth)) {
	            monthCounts.put(yearMonth, monthCounts.get(yearMonth) + 1);
	        } else {
	            monthCounts.put(yearMonth, 1);
	        }
	    }

	    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	    
	    for (Map.Entry<String, Integer> entry : monthCounts.entrySet()) {
	    	PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
	        data.nodeProperty().addListener(new ChangeListener<Node>() {
	        	@Override
	            public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
	                if (newNode != null) {
	                    Tooltip tooltip = new Tooltip("Numbers of articles: " + (int)data.getPieValue());
	                    Tooltip.install(newNode, tooltip);
	                }
	            }
	        });
	        pieChartData.add(data);
	    }
	    pieChart.setData(pieChartData);
	}

}
