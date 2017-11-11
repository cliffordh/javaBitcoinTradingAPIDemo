package com.bitso.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderBookModel {
    // contains snapshot of bids/asks
    // contains diff-orders queue

    private List<DiffOrder> diffOrders;
    private ObservableList<Bid> bids;
    private ObservableList<Ask> asks;
    private HashMap<String, Bid> bidHashMap;
    private HashMap<String, Ask> askHashMap;

    private Payload payload;

    public OrderBookModel() {
        diffOrders = new ArrayList<DiffOrder>();
        bids = FXCollections.observableArrayList();
        asks = FXCollections.observableArrayList();
        bidHashMap = new HashMap<>();
        askHashMap = new HashMap<>();
    }

    public void addDiffOrder(DiffOrder o) {
        diffOrders.add(o);
        System.out.println("Added diff-order. Count is " + diffOrders.size());

        if (payload != null) {

            synchronized (diffOrders) {
                List<DiffOrder> toRemove = new ArrayList<>();

                // iterate diffOrders
                for (DiffOrder diff : diffOrders) {
                    if (diff.getSequence() < payload.getSequence()) {
                        System.out.println("Removing diff sequence: " + diff.getSequence());
                        toRemove.add(diff);
                        continue;
                    }
                    String oid = diff.getPayload().get(0).getO();
                    String op = diff.getPayload().get(0).getS();
                    Double amt = diff.getPayload().get(0).getA();
                    Double r = diff.getPayload().get(0).getR();

                    Bid b = bidHashMap.get(oid);
                    Ask a = askHashMap.get(oid);

                    if (op.equals("open")) {
                       if(b!=null) {
                           b.setAmount(amt);
                           b.setPrice(r);
                       }
                       if(a!=null) {
                           a.setAmount(amt);
                           a.setPrice(r);
                       }
                    } else if(op.equals("cancelled")) {
                        bids.remove(b);
                        asks.remove(a);
                        bidHashMap.remove(oid);
                        askHashMap.remove(oid);
                    }
                    toRemove.add(diff); // when done processing remove it
                }
                // process removals to avoid ConcurrentModificationException
                diffOrders.removeAll(toRemove);
            }
        }

    }

    public void setPayload(Payload payload) {
        this.payload = payload;
        asks.addAll(this.payload.getAsks());
        bids.addAll(this.payload.getBids());

        // this will enable lookup of the bid or ask to update based on the diff-order instruction
        bids.forEach(bid -> bidHashMap.put(bid.getOid(), bid));
        asks.forEach(ask -> askHashMap.put(ask.getOid(), ask));
    }

    public ObservableList<Bid> getObservableBids() {
        return bids;
    }

    public ObservableList<Ask> getObservableAsks() {
        return asks;
    }
}
