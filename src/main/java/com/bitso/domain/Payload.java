package com.bitso.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payload {

        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("bids")
        @Expose
        private List<Bid> bids = null;
        @SerializedName("asks")
        @Expose
        private List<Ask> asks = null;
        @SerializedName("sequence")
        @Expose
        private String sequence;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<Bid> getBids() {
            return bids;
        }

        public void setBids(List<Bid> bids) {
            this.bids = bids;
        }

        public List<Ask> getAsks() {
            return asks;
        }

        public void setAsks(List<Ask> asks) {
            this.asks = asks;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

}
