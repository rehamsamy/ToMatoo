package com.tomatoo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactMessagesModel {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("messages")
    private List<ContactMsgItem> messages;

    public Boolean getStatus() {
        return status;
    }

    public List<ContactMsgItem> getMessages() {
        return messages;
    }


    public static class ContactMsgItem implements Parcelable {
        @SerializedName("msg_id")
        private int msg_id;
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
        private int msg_show;
        @SerializedName("data")
        private String data;
        @SerializedName("user_id")
        private int user_id;


        protected ContactMsgItem(Parcel in) {
            msg_id = in.readInt();
            msg_name = in.readString();
            msg_email = in.readString();
            msg_mobile = in.readString();
            msg_title = in.readString();
            msg_message = in.readString();
            msg_status = in.readString();
            msg_show = in.readInt();
            data = in.readString();
            user_id = in.readInt();
        }

        public static final Creator<ContactMsgItem> CREATOR = new Creator<ContactMsgItem>() {
            @Override
            public ContactMsgItem createFromParcel(Parcel in) {
                return new ContactMsgItem(in);
            }

            @Override
            public ContactMsgItem[] newArray(int size) {
                return new ContactMsgItem[size];
            }
        };

        public int getMsg_id() {
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

        public int getMsg_show() {
            return msg_show;
        }

        public String getData() {
            return data;
        }

        public int getUser_id() {
            return user_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(msg_id);
            dest.writeString(msg_name);
            dest.writeString(msg_email);
            dest.writeString(msg_mobile);
            dest.writeString(msg_title);
            dest.writeString(msg_message);
            dest.writeString(msg_status);
            dest.writeInt(msg_show);
            dest.writeString(data);
            dest.writeInt(user_id);
        }
    }
}
