package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocialMediaModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("socialmedia")
    private List<MediaData> socialmedia;

    //Getter
    public Boolean getStatus() {
        return status;
    }

    public List<MediaData> getSocialmedia() {
        return socialmedia;
    }


    // class MediaData...
    public static class MediaData {
        @SerializedName("link")
        private String link;
        @SerializedName("name")
        private String name;

        // Gettters
        public String getLink() {
            return link;
        }

        public String getName() {
            return name;
        }
    }
}
