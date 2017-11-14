package com.bitso.example;

import com.bitso.domain.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/* Controller will coordinate networking and model updates. */
public class Controller {

    private OrderBookModel orderBookModel;
    private TradesModel tradesModel;
    private WSClient wsClient;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(2);

    private APIResponse apiResponse;
    private static final String ORDER_URL = "https://api.bitso.com/v3/order_book/?book=btc_mxn&aggregate=false";
    private static final String TRADES_URL = "https://api.bitso.com/v3/trades/?book=btc_mxn";

    private Main main;

    public OrderBookModel getOrderBookModel() {
        return orderBookModel;
    }

    public TradesModel getTradesModel() {
        return tradesModel;
    }

    public void init(Main main) {
        // create OrderBookModel
        // open websocket, pass an instance of this to receive messages
        // fetch OrderBook from RESTish endpoint
        this.main = main; // use for GUI callbacks
        orderBookModel = new OrderBookModel();
        tradesModel = new TradesModel();
        wsClient = new WSClient(this);
        wsClient.connect();
        // use executorservice for one off run
        executorService.submit(fetchOrders);
        // use scheduledservice for repeated run
        scheduledService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    String tradesJSON = readUrl(TRADES_URL);
                    TradeResponse response = new Gson().fromJson(tradesJSON, TradeResponse.class);
                    tradesModel.setTradeResponse(response);
                    //System.out.println(tradesJSON);
                    //System.out.println(response.getPayload().size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        },0,1,TimeUnit.SECONDS);

        fetchOrders.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                apiResponse = fetchOrders.getValue();
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
        scheduledService.shutdown();
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
     * Task to fetch orders from ORDER_URL
     * @param <V>
     */
    private Task<APIResponse> fetchOrders = new Task() {
        @Override
        protected APIResponse call() throws Exception {
            APIResponse response = null;
            try {
                Gson gson = new Gson();
                response = new Gson().fromJson(readUrl(ORDER_URL), APIResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    };

    /**
     * Task to fetch trades from TRADES_URL
     * @param <V>
     */
    private Task<APIResponse> fetchTrades = new Task() {
        @Override
        protected APIResponse call() throws Exception {
            APIResponse response = null;
            return response;
        }
    };

}
