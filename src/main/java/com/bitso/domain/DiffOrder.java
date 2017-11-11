package com.bitso.domain;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiffOrder {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("book")
    @Expose
    private String book;
    @SerializedName("payload")
    @Expose
    private List<DiffPayload> payload = null;
    @SerializedName("sequence")
    @Expose
    private Integer sequence;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public List<DiffPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<DiffPayload> payload) {
        this.payload = payload;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
