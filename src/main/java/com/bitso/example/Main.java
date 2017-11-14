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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.bitso.domain.*;

public class Main extends Application {

    private Controller appController;

    public TableView getBidTableView() {
        return bidTableView;
    }

    private TableView bidTableView = new TableView();

    public TableView getAskTableView() {
        return askTableView;
    }

    private TableView askTableView = new TableView();
    private TableView tradeTableView = new TableView();

    final TextField XtextField = new TextField();
    final TextField MtextField = new TextField();
    final TextField NtextField = new TextField();
    private Button applyButton = new Button();

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

        bidTableView.setItems(appController.getOrderBookModel().getSortedFilteredObservableBids());

        TableColumn askCol = new TableColumn("Ask");
        TableColumn askAmountCol = new TableColumn("Amount");

        askTableView.getColumns().addAll(askCol, askAmountCol);
        askTableView.setMaxWidth(200);
        askTableView.setMaxHeight(300);

        askCol.setCellValueFactory(new PropertyValueFactory<Ask,String>("price"));
        askAmountCol.setCellValueFactory(new PropertyValueFactory<Ask,String>("amount"));

        askTableView.setItems(appController.getOrderBookModel().getSortedFilteredObservableAsks());

        TableColumn descriptionCol = new TableColumn("Recent Trades");
        tradeTableView.getColumns().addAll(descriptionCol);
        tradeTableView.setMaxWidth(420);
        tradeTableView.setMaxHeight(200);

        descriptionCol.setCellValueFactory(new PropertyValueFactory<TradePayload,String>("description"));

        tradeTableView.setItems(appController.getTradesModel().getTrades());

        grid.add(bidTableView,0,1);
        grid.add(askTableView,1,1);
        grid.add(tradeTableView,0,2,2,1);

        //Creating a GridPane container
        GridPane parametersGrid = new GridPane();
        parametersGrid.setPadding(new Insets(10, 10, 10, 10));
        parametersGrid.setVgap(5);
        parametersGrid.setHgap(5);

        XtextField.setPromptText("X");
        XtextField.setPrefColumnCount(3);
        XtextField.getText();
        GridPane.setConstraints(XtextField, 0, 0);
        parametersGrid.getChildren().add(XtextField);

        MtextField.setPrefColumnCount(2);
        MtextField.setPromptText("M");
        GridPane.setConstraints(MtextField, 0, 1);
        parametersGrid.getChildren().add(MtextField);

        NtextField.setPrefColumnCount(2);
        NtextField.setPromptText("N");
        GridPane.setConstraints(NtextField, 0, 2);
        parametersGrid.getChildren().add(NtextField);

        applyButton = new Button("Apply");
        GridPane.setConstraints(applyButton, 1, 0);
        parametersGrid.getChildren().add(applyButton);
        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    if(!XtextField.getText().isEmpty())
                    {
                        appController.getOrderBookModel().setMaxBidAsks(Integer.parseInt(XtextField.getText()));
                    }
                    if(!MtextField.getText().isEmpty())
                    {
                        appController.getTradesModel().setMupticks(Integer.parseInt(MtextField.getText()));
                    }
                    if(!NtextField.getText().isEmpty()) {
                        appController.getTradesModel().setNdownticks(Integer.parseInt(NtextField.getText()));
                    }
                    askTableView.refresh();
                    bidTableView.refresh();
                } catch (Exception ex)
                {
                    System.out.println("Invalid Input");
                }
            }
        });

        grid.add(parametersGrid,0,3,2,1);
        Label infoText = new Label("Changes apply on next update. Defaults X=999, M=3, N=2");
        grid.add(infoText,0,4,2,1);

        ScrollPane scrollPane = new ScrollPane(grid);

        Scene scene = new Scene(scrollPane, 440, 700);
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
