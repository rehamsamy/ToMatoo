package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentInfoModel {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("Paynment")
    private List<PaymentInfo> Paynment;

    //Getter
    public Boolean getStatus() {
        return status;
    }

    public List<PaymentInfo> getPaynment() {
        return Paynment;
    }

    //Class PaymentInfo
    public static class PaymentInfo {
        @SerializedName("Key")
        private String Key;
        @SerializedName("Value")
        private String Value;

        // Getters...
        public String getKey() {
            return Key;
        }

        public String getValue() {
            return Value;
        }
    }
}
