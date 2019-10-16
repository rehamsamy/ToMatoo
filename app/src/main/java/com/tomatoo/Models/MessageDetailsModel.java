package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageDetailsModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("Message")
    private MessageData Message;

    //Getter
    public Boolean getStatus() {
        return status;
    }

    public MessageData getMessage() {
        return Message;
    }


    // class MessageData
    public static class MessageData {
        @SerializedName("main_Message")
        private MainMessage main_Message;
        @SerializedName("sub_Message")
        private List<SubMessage> sub_Message;

        // Getter
        public MainMessage getMain_Message() {
            return main_Message;
        }

        public List<SubMessage> getSub_Message() {
            return sub_Message;
        }
    }

    // class MainMessage
    public static class MainMessage {
        @SerializedName("msg_id")
        private Integer msg_id;
        @SerializedName("msg_name")
        private String msg_name;
        @SerializedName("msg_email")
        private String msg_email;
        @SerializedName("msg_mobile")
        private String msg_mobile;
        @SerializedName("msg_title")
        private String msg_title;
        @SerializedName("msg_message")
        private String msg_message;
        @SerializedName("msg_status")
        private String msg_status;
        @SerializedName("msg_show")
        private Integer msg_show;
        @SerializedName("data")
        private String data;
        @SerializedName("user_id")
        private Integer user_id;

        // Getter
        public Integer getMsg_id() {
            return msg_id;
        }

        public String getMsg_name() {
            return msg_name;
        }

        public String getMsg_email() {
            return msg_email;
        }

        public String getMsg_mobile() {
            return msg_mobile;
        }

        public String getMsg_title() {
            return msg_title;
        }

        public String getMsg_message() {
            return msg_message;
        }

        public String getMsg_status() {
            return msg_status;
        }

        public Integer getMsg_show() {
            return msg_show;
        }

        public String getData() {
            return data;
        }

        public Integer getUser_id() {
            return user_id;
        }
    }

    // class SubMessage
    public static class SubMessage {
        @SerializedName("msg_id")
        private Integer msg_id;
        @SerializedName("msg_name")
        private String msg_name;
        @SerializedName("msg_email")
        private String msg_email;
        @SerializedName("msg_mobile")
        private String msg_mobile;
        @SerializedName("msg_title")
        private String msg_title;
        @SerializedName("msg_message")
        private String msg_message;
        @SerializedName("msg_status")
        private String msg_status;
        @SerializedName("msg_show")
        private Integer msg_show;
        @SerializedName("data")
        private String data;
        @SerializedName("user_id")
        private Integer user_id;

        // Getter
        public Integer getMsg_id() {
            return msg_id;
        }

        public String getMsg_name() {
            return msg_name;
        }

        public String getMsg_email() {
            return msg_email;
        }

        public String getMsg_mobile() {
            return msg_mobile;
        }

        public String getMsg_title() {
            return msg_title;
        }

        public String getMsg_message() {
            return msg_message;
        }

        public String getMsg_status() {
            return msg_status;
        }

        public Integer getMsg_show() {
            return msg_show;
        }

        public String getData() {
            return data;
        }

        public Integer getUser_id() {
            return user_id;
        }
    }
}
