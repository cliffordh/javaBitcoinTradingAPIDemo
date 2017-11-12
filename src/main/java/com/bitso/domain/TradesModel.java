package com.bitso.domain;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TradesModel {
    private TradeResponse response;
    private ObservableList<TradePayload> trades;

    public TradesModel() {
        trades = FXCollections.observableArrayList();
    }
    public ObservableList<TradePayload> getTrades() {
        return trades;
    }

    public void setTradeResponse(TradeResponse response) {
        this.response = response;
        trades.setAll(response.getPayload());
    }
}
