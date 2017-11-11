package com.bitso.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.Observable;
import javafx.util.Callback;
import javafx.collections.transformation.SortedList;

public class OrderBookModel {
    // contains snapshot of bids/asks
    // contains diff-orders queue

    private List<DiffOrder> diffOrders;
    private ObservableList<Bid> bids;
    private ObservableList<Ask> asks;
    private SortedList<Bid> sortedBids;
    private SortedList<Ask> sortedAsks;

    private HashMap<String, Bid> bidHashMap;
    private HashMap<String, Ask> askHashMap;

    private Payload payload;

    public OrderBookModel() {
        diffOrders = new ArrayList<DiffOrder>();
        bids = FXCollections.observableArrayList(
                new Callback<Bid, Observable[]>() {
                    @Override
                    public Observable[] call(Bid param) {
                        return new Observable[]{
                                param.getAmountProperty(),
                                param.getPriceProperty()
                        };
                    }
                }
        );
        asks = FXCollections.observableArrayList(
                new Callback<Ask, Observable[]>() {
                    @Override
                    public Observable[] call(Ask param) {
                        return new Observable[]{
                                param.getAmountProperty(),
                                param.getPriceProperty()
                        };
                    }
                }
        );

        // sort into the lowest ask and the highest bid, i.e. best ask, best bid
        sortedBids = new SortedList<>( bids,
                (Bid bid1, Bid bid2) -> {
                    if( Double.parseDouble(bid1.getPrice()) < Double.parseDouble(bid2.getPrice()) ) {
                        return 1; // note this is REVERSE logic compared w/ the asks
                    } else if( Double.parseDouble(bid1.getPrice()) > Double.parseDouble(bid2.getPrice()) ) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
        sortedAsks = new SortedList<>( asks,
                (Ask ask1, Ask ask2) -> {
                    if( Double.parseDouble(ask1.getPrice()) < Double.parseDouble(ask2.getPrice())) {
                        return -1;
                    } else if( Double.parseDouble(ask1.getPrice()) > Double.parseDouble(ask2.getPrice()) ) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
        bidHashMap = new HashMap<>();
        askHashMap = new HashMap<>();
    }

    public void addDiffOrder(DiffOrder o) {
        diffOrders.add(o);
//        System.out.println("Added diff-order. Count is " + diffOrders.size());

        if (payload != null) {

            synchronized (diffOrders) {
                List<DiffOrder> toRemove = new ArrayList<>();

                // iterate diffOrders
                for (DiffOrder diff : diffOrders) {
                    if (diff.getSequence() < payload.getSequence()) {
//                        System.out.println("Removing diff sequence: " + diff.getSequence());
                        toRemove.add(diff);
                        continue;
                    }
                    String oid = diff.getPayload().get(0).getO();
                    String op = diff.getPayload().get(0).getS();
                    String amt = diff.getPayload().get(0).getA();
                    String r = diff.getPayload().get(0).getR();
                    Integer t = diff.getPayload().get(0).getT();

                    Bid b = bidHashMap.get(oid);
                    Ask a = askHashMap.get(oid);

                    if (op.equals("open")) {
                        if (b != null) {
                            b.setAmount(amt);
                            b.setPrice(r);
                        } else if (a != null) {
                            a.setAmount(amt);
                            a.setPrice(r);
                        } else {
                            // if t == 0 then it's a bid/buy, else ask, create new and add it to the data structure
                            switch (t) {
                                case 0:
                                    b = new Bid();
                                    b.setPrice(r);
                                    b.setAmount(amt);
                                    b.setOid(oid);
                                    bidHashMap.put(oid, b);
                                    bids.add(b);
                                    break;
                                case 1:
                                    a = new Ask();
                                    a.setPrice(r);
                                    a.setAmount(amt);
                                    a.setOid(oid);
                                    askHashMap.put(oid, a);
                                    asks.add(a);
                                    break;
                            }
//                            System.out.println("NEW ORDER");
                        }
                    } else if (op.equals("cancelled")) {
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

    public SortedList<Bid> getSortedObservableBids() {
        return sortedBids;
    }
    public SortedList<Ask> getSortedObservableAsks() {
        return sortedAsks;
    }
}
