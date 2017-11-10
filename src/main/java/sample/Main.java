package sample;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.HttpURLConnection;

public class Main extends Application {

    private APIResponse apiResponse;
    private static final String JSON_URL = "https://api.bitso.com/v3/order_book/?book=btc_mxn&aggregate=false";
    private static final Integer MAX_BIDASKS = 10; // make a command line parameter
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private WSClient wsClient;

    @Override
    public void start(Stage stage) throws Exception {

        wsClient = new WSClient();
        wsClient.connect();

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setSpacing(20);

        Label major_minor = new Label("BTCMXN");

        root.getChildren().add(major_minor);

//        button.setOnAction(e -> {
            executorService.submit(fetchResponse);
//        });

        fetchResponse.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                apiResponse = fetchResponse.getValue();
                GridPane gridPane = createGridPane(apiResponse);
                //root.getChildren().remove(1);
               VBox.setVgrow(gridPane, Priority.ALWAYS);
                root.getChildren().add(gridPane);
              //  stage.sizeToScene();
            }
        });

        ScrollPane scrollPane = new ScrollPane(root);
        Scene scene = new Scene(scrollPane, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Bitso Trading Programming Challenge");
        stage.show();

        stage.setOnCloseRequest(e -> {
            executorService.shutdown();
        });
    }

    public GridPane createGridPane(APIResponse apiResponse){
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(true);
//        gridPane.setPadding(new Insets(20));
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

        List<Ask> asks = apiResponse.getPayload().getAsks();
        List<Bid> bids = apiResponse.getPayload().getBids();

        for (int i = 0; i < asks.size() && i < bids.size() && i<MAX_BIDASKS;i++) {
           Ask askRow = asks.get(i);
           Bid bidRow = bids.get(i);
           Label bidLabel = new Label(bidRow.getPrice());
           Label bidAmountLabel = new Label(bidRow.getAmount());
           Label askLabel = new Label(askRow.getPrice());
           Label askAmountLabel = new Label(askRow.getAmount());

           gridPane.add(bidLabel,0,i+1);
           gridPane.add(bidAmountLabel,1,i+1);
           gridPane.add(askLabel,2,i+1);
           gridPane.add(askAmountLabel,3,i+1);

           alignElements(bidLabel, bidAmountLabel, askLabel, askAmountLabel);

           gridPane.getRowConstraints().add(new RowConstraints(20));

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

    /**
     * Task to fetch details from JSONURL
     * @param <V>
     */
    private Task<APIResponse> fetchResponse = new Task() {
        @Override
        protected APIResponse call() throws Exception {
            APIResponse response = null;
            try {
                Gson gson = new Gson();
                response = new Gson().fromJson(readUrl(JSON_URL), APIResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    };

    /**
     * Read the URL and return the json data
     * @param urlString
     * @return
     * @throws Exception
     */
    private static String readUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // fake the user agent
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.1.2 Safari/603.3.8");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }

    private class Ask {

        @SerializedName("book")
        @Expose
        private String book;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("amount")
        @Expose
        private String amount;

        @SerializedName("oid")
        @Expose
        private String oid;

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getOid() {
            return oid;
        }
        public void setOid(String oid) {
            this.oid = oid;
        }

    }

    private class Bid {

        @SerializedName("book")
        @Expose
        private String book;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("oid")
        @Expose
        private String oid;

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getOid() {
            return oid;
        }
        public void setOid(String oid) {
            this.oid = oid;
        }

    }

    private class APIResponse {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("payload")
        @Expose
        private Payload payload;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }

    }

    private class Payload {

        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("bids")
        @Expose
        private List<Bid> bids = null;
        @SerializedName("asks")
        @Expose
        private List<Ask> asks = null;
        @SerializedName("sequence")
        @Expose
        private String sequence;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<Bid> getBids() {
            return bids;
        }

        public void setBids(List<Bid> bids) {
            this.bids = bids;
        }

        public List<Ask> getAsks() {
            return asks;
        }

        public void setAsks(List<Ask> asks) {
            this.asks = asks;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
