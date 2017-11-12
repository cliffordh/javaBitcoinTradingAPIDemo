
package com.bitso.domain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradeResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("payload")
    @Expose
    private List<TradePayload> payload = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<TradePayload> getPayload() {
        return payload;
    }

    public void setPayload(List<TradePayload> payload) {
        this.payload = payload;
    }

}
