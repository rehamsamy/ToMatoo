package com.example.khaled_sa2r.tomatoo;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {
   String name,type,price,description,image;


    public ItemModel(String name, String type, String price, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    protected ItemModel(Parcel in) {
        name = in.readString();
        type = in.readString();
        price = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(image);
    }
}
