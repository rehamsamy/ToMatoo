package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

public class HowToUseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("how_to_use")
    private HowToUseData how_to_use;

    // Getters ...
    public Boolean getStatus() {
        return status;
    }

    public HowToUseData getHow_to_use() {
        return how_to_use;
    }


    public class HowToUseData {
        @SerializedName("desc_en")
        private String desc_en;
        @SerializedName("desc_ar")
        private String desc_ar;

        // Getter ...
        public String getDesc_en() {
            return desc_en;
        }

        public String getDesc_ar() {
            return desc_ar;
        }
    }
}
