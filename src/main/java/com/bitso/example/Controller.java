package com.bitso.example;

import com.bitso.domain.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import java.util.List;

/* Controller will coordinate networking and model updates. */
public class Controller {

    private OrderBookModel orderBookModel;
    private TradesModel tradesModel;
    private WSClient wsClient;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private APIResponse apiResponse;
    private static final String JSON_URL = "https://api.bitso.com/v3/order_book/?book=btc_mxn&aggregate=false";

    private Main main;

    public OrderBookModel getOrderBookModel() {
        return orderBookModel;
    }

    public void init(Main main) {
        // create OrderBookModel
        // open websocket, pass an instance of this to receive messages
        // fetch OrderBook from RESTish endpoint
        this.main = main; // use for GUI callbacks
        orderBookModel = new OrderBookModel();
        wsClient = new WSClient(this);
        wsClient.connect();
        executorService.submit(fetchResponse);

        fetchResponse.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                apiResponse = fetchResponse.getValue();
                orderBookModel.setPayload(apiResponse.getPayload());
            }
        });
    }

    public void diffOrderReceived(String json) {
        DiffOrder response = null;
        try {
            Gson gson = new Gson();
            response = new Gson().fromJson(json, DiffOrder.class);
            orderBookModel.addDiffOrder(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

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

}
