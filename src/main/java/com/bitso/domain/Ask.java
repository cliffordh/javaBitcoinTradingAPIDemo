package com.bitso.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ask {
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

    private StringProperty amountProperty=new SimpleStringProperty();
    private StringProperty priceProperty=new SimpleStringProperty();

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
        this.price=price;
        this.priceProperty.set(price);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount=amount;
        this.amountProperty.set(amount);
    }

    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }

    public StringProperty getPriceProperty() {
        return priceProperty;
    }
    public StringProperty getAmountProperty() {
        return amountProperty;
    }
}
