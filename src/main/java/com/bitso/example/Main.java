package com.bitso.example;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import com.bitso.domain.*;

public class Main extends Application {

    private static final Integer MAX_BIDASKS = 10; // make a command line parameter
    private Controller appController;
    private GridPane orderBookPane;
    private TableView bidTableView = new TableView();
    private TableView askTableView = new TableView();

    @Override
    public void start(Stage stage) throws Exception {

        appController = new Controller();
        appController.init(this);

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setSpacing(20);

        Label major_minor = new Label("BTCMXN");

        root.getChildren().add(major_minor);

        orderBookPane = createGridPane();

        TableColumn bidCol = new TableColumn("Bid");
        TableColumn bidAmountCol = new TableColumn("Amount");

        bidTableView.getColumns().addAll(bidCol, bidAmountCol);

        TableColumn askCol = new TableColumn("Ask");
        TableColumn askAmountCol = new TableColumn("Amount");

        askTableView.getColumns().addAll(askCol, askAmountCol);

        VBox.setVgrow(orderBookPane, Priority.ALWAYS);
        root.getChildren().addAll(orderBookPane,bidTableView,askTableView);

        ScrollPane scrollPane = new ScrollPane(root);
        Scene scene = new Scene(scrollPane, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Bitso Trading Programming Challenge");
        stage.show();

        stage.setOnCloseRequest(e -> {
            appController.shutdown();
        });
    }

    public void updateGridPane(List<Bid> bids, List<Ask> asks) {
        // replace with a better implementation of the Observable pattern

        for (int i = 0; i < asks.size() && i < bids.size() && i<MAX_BIDASKS;i++) {
           Ask askRow = asks.get(i);
           Bid bidRow = bids.get(i);

           Label bidLabel = new Label(bidRow.getPrice());
           Label bidAmountLabel = new Label(bidRow.getAmount());
           Label askLabel = new Label(askRow.getPrice());
           Label askAmountLabel = new Label(askRow.getAmount());

           orderBookPane.add(bidLabel,0,i+1);
           orderBookPane.add(bidAmountLabel,1,i+1);
           orderBookPane.add(askLabel,2,i+1);
           orderBookPane.add(askAmountLabel,3,i+1);

           alignElements(bidLabel, bidAmountLabel, askLabel, askAmountLabel);

           orderBookPane.getRowConstraints().add(new RowConstraints(20));

        }

    }

    public GridPane createGridPane(){
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(true);
        gridPane.setMinHeight(200);
        gridPane.setMaxWidth(200);

        //Create headings
        Label bidHeading = new Label("Bid");
        bidHeading.setStyle("-fx-font-weight: bold");
        Label bidAmountHeading = new Label("Amount");
        bidAmountHeading.setStyle("-fx-font-weight: bold");
        Label askHeading = new Label("Ask");
        askHeading.setStyle("-fx-font-weight: bold");
        Label askAmountHeading = new Label("Amount");
        askAmountHeading.setStyle("-fx-font-weight: bold");

        gridPane.add(bidHeading, 0, 0);
        gridPane.add(bidAmountHeading, 1, 0);
        gridPane.add(askHeading, 2, 0);
        gridPane.add(askAmountHeading, 3, 0);

        // Aligning at center
        alignElements(bidHeading, bidAmountHeading, askHeading, askAmountHeading);

        // Setting Constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(110);
            gridPane.getColumnConstraints().add(column);
       }
        return gridPane;
    }

    /**
     * Align elements at the center
     * @param nodes
     */
    private void alignElements(Node ... nodes ) {
        for(Node node : nodes) {
            GridPane.setHalignment(node, HPos.CENTER);
            GridPane.setValignment(node, VPos.CENTER);
        }
    }


  public static void main(String[] args) {
        launch(args);
    }
}
