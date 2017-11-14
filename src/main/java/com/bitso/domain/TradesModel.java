package com.bitso.domain;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;

public class TradesModel {
    private TradeResponse response;
    private ObservableList<TradePayload> trades;

    private int Mupticks;   // configurable parameters that specify when contrarian trades are made
    private int Ndownticks;

    public int getMupticks() {
        return Mupticks;
    }

    public void setMupticks(int mupticks) {
        Mupticks = mupticks;
    }

    public int getNdownticks() {
        return Ndownticks;
    }

    public void setNdownticks(int ndownticks) {
        Ndownticks = ndownticks;
    }

    private int upticks; // records consecutive upticks
    private int downticks; // records consecutive downticks

    public TradesModel() {
        trades = FXCollections.observableArrayList();
        setMupticks(3); // reasonable defaults for contrarian algorithm
        setNdownticks(2);
    }

    public ObservableList<TradePayload> getTrades() {
        return trades;
    }

    public void setTradeResponse(TradeResponse response) {

        Collections.reverse(response.getPayload());
        int uptick=0;
        int downtick=0;
        String prevprice="";
        int counter = 0;

        List<TradePayload> algoAndNaturalTrades=new ArrayList<TradePayload>();

        for (TradePayload p : response.getPayload()) {
            algoAndNaturalTrades.add(p);
            counter = counter + 1;
            if (counter==1) {
                prevprice = p.getPrice(); // prime the tick counter
                continue;
            }
            if (p.getPrice()==prevprice) {
                continue; // same price doesn't affect up/downticks
            }
            if (Double.parseDouble(p.getPrice())<Double.parseDouble(prevprice)) {
                // most recent price is less than previous price... this is a downtick
                downtick = downtick + 1;
                uptick = 0;
            } else {
                downtick = 0;
                uptick = uptick + 1;
            }
            //System.out.println(p.getDescription());
            //System.out.println("Uptick: "+uptick+" Downtick: "+downtick);
            TradePayload q = null;
            if(uptick>=Mupticks) { // insert a sell
                q=new TradePayload();
                q.setAmount("1.0");
                q.setPrice(p.getPrice());
                q.setBook("btc_mxn");
                q.setTid(p.getTid()+10000000);
                q.setMakerSide("sell");
            } else if (downtick>=Ndownticks) { // insert a buy
                q=new TradePayload();
                q.setAmount("1.0");
                q.setPrice(p.getPrice());
                q.setBook("btc_mxn");
                q.setTid(p.getTid()+10000000);
                q.setMakerSide("buy");
                prevprice=p.getPrice();
            }
            if (q!=null)
            {
                algoAndNaturalTrades.add(q);
            }
        }

        this.response = response;
        Collections.reverse(algoAndNaturalTrades);
        trades.setAll(algoAndNaturalTrades);
    }
}
