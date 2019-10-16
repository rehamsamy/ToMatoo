package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

public class SendSuggestionModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("messages")
    private String messages;


    // Getters ...
    public Boolean getStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }
}
