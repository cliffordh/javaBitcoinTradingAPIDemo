package com.bitso.example;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.bitso.domain.*;

public class Main extends Application {

    private static final Integer MAX_BIDASKS = 10; // make a command line parameter
    private Controller appController;
    private TableView bidTableView = new TableView();
    private TableView askTableView = new TableView();
    private TableView tradeTableView = new TableView();

    @Override
    public void start(Stage stage) throws Exception {

        appController = new Controller();
        appController.init(this);

        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        Label major_minor = new Label("BTCMXN");
        GridPane.setHalignment(major_minor, HPos.CENTER);

        grid.add(major_minor,0,0,2,1);

        TableColumn bidCol = new TableColumn("Bid");
        TableColumn bidAmountCol = new TableColumn("Amount");

        bidTableView.getColumns().addAll(bidCol, bidAmountCol);
        bidTableView.setMaxWidth(200);
        bidTableView.setMaxHeight(300);

        bidCol.setCellValueFactory(new PropertyValueFactory<Bid,String>("price"));
        bidAmountCol.setCellValueFactory(new PropertyValueFactory<Bid,String>("amount"));

        bidTableView.setItems(appController.getOrderBookModel().getSortedObservableBids());

        TableColumn askCol = new TableColumn("Ask");
        TableColumn askAmountCol = new TableColumn("Amount");

        askTableView.getColumns().addAll(askCol, askAmountCol);
        askTableView.setMaxWidth(200);
        askTableView.setMaxHeight(300);

        askCol.setCellValueFactory(new PropertyValueFactory<Ask,String>("price"));
        askAmountCol.setCellValueFactory(new PropertyValueFactory<Ask,String>("amount"));

        askTableView.setItems(appController.getOrderBookModel().getSortedObservableAsks());

        TableColumn descriptionCol = new TableColumn("Recent Trades");
        tradeTableView.getColumns().addAll(descriptionCol);
        tradeTableView.setMaxWidth(420);
        tradeTableView.setMaxHeight(200);

        descriptionCol.setCellValueFactory(new PropertyValueFactory<TradePayload,String>("description"));

        tradeTableView.setItems(appController.getTradesModel().getTrades());

        grid.add(bidTableView,0,1);
        grid.add(askTableView,1,1);
        grid.add(tradeTableView,0,2,2,1);

        ScrollPane scrollPane = new ScrollPane(grid);

        Scene scene = new Scene(scrollPane, 440, 600);
        stage.setScene(scene);
        stage.setTitle("Bitso Trading Programming Challenge");
        stage.show();

        stage.setOnCloseRequest(e -> {
            appController.shutdown();
        });
    }

  public static void main(String[] args) {
        launch(args);
    }
}
