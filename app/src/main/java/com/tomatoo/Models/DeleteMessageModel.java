package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

public class DeleteMessageModel {

    @SerializedName("SuccessMsg")
    private String SuccessMsg;

    public String getSuccessMsg() {
        return SuccessMsg;
    }
}
