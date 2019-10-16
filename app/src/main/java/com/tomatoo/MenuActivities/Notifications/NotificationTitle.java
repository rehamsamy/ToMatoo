package com.tomatoo.MenuActivities.Notifications;

import com.google.gson.annotations.SerializedName;

public class NotificationTitle {

    @SerializedName("title")
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
