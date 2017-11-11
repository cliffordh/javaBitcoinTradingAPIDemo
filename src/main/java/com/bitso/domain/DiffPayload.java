package com.bitso.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiffPayload {

    @SerializedName("o")
    @Expose
    private String o;
    @SerializedName("d")
    @Expose
    private Long d;
    @SerializedName("r")
    @Expose
    private String r;
    @SerializedName("t")
    @Expose
    private Integer t;
    @SerializedName("a")
    @Expose
    private String a;
    @SerializedName("v")
    @Expose
    private String v;
    @SerializedName("s")
    @Expose
    private String s;

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public Long getD() {
        return d;
    }

    public void setD(Long d) {
        this.d = d;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

}
