package com.bitso.domain;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class OrderBookModel {
    // contains snapshot of bids/asks
    // contains diff-orders queue

    private List<DiffOrder> diffOrders;
    private List<Bid> bids;
    private List<Ask> asks;

    private Payload payload;

    public OrderBookModel() {
        diffOrders = new ArrayList<DiffOrder>();
        bids = new ArrayList<Bid>();
        asks = new ArrayList<Ask>();
    }

    public void addDiffOrder(DiffOrder o) {
       diffOrders.add(o);
       System.out.println("Added diff-order. Count is"+diffOrders.size());
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
        asks = this.payload.getAsks();
        bids = this.payload.getBids();
    }

    public List<Bid> getBids() {
        return bids;
    }
    public List<Ask> getAsks() {
        return asks;
    }
}
