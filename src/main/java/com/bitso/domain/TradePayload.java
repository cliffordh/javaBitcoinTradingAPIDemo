package com.bitso.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradePayload {

    @SerializedName("book")
    @Expose
    private String book;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("maker_side")
    @Expose
    private String makerSide;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("tid")
    @Expose
    private Integer tid;

    public String getDescription() {
        return "[ "+tid+" ] "+makerSide.toUpperCase()+" "+amount+" BTC @ "+price+" MXN ";
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMakerSide() {
        return makerSide;
    }

    public void setMakerSide(String makerSide) {
        this.makerSide = makerSide;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

}
