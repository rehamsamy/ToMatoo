package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

public class AboutModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("page")
    private String page;

    // Gettters
    public Boolean getStatus() {
        return status;
    }

    public String getPage() {
        return page;
    }
}
