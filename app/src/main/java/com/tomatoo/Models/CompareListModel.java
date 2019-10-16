package com.tomatoo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompareListModel {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("products")
    private List<CompareProduct> products;

    // Getters ...
    public Boolean getStatus() {
        return status;
    }

    public List<CompareProduct> getProducts() {
        return products;
    }


    // class CompareProduct ...
    public static class CompareProduct {
        @SerializedName("Compare_id")
        private Integer Compare_id;
        @SerializedName("Product_id")
        private Integer Product_id;
        @SerializedName("Categoryid")
        private Integer Categoryid;
        @SerializedName("IsFeatured")
        private Integer IsFeatured;
        @SerializedName("SubCategoryid")
        private Integer SubCategoryid;
        @SerializedName("product_name_ar")
        private String product_name_ar;
        @SerializedName("product_name_en")
        private String product_name_en;
        @SerializedName("Price")
        private Integer Price;
        @SerializedName("photo")
        private String photo;
        @SerializedName("category_name_ar")
        private String category_name_ar;
        @SerializedName("category_name_en")
        private String category_name_en;
        @SerializedName("sub_category_name_en")
        private String sub_category_name_en;
        @SerializedName("sub_category_name_ar")
        private String sub_category_name_ar;
        @SerializedName("wishlists")
        private Integer wishlists;
        @SerializedName("cart")
        private Integer cart;

        // Getter...

        public void setCompare_id(Integer compare_id) {
            Compare_id = compare_id;
        }

        public void setProduct_id(Integer product_id) {
            Product_id = product_id;
        }

        public void setCategoryid(Integer categoryid) {
            Categoryid = categoryid;
        }

        public void setIsFeatured(Integer isFeatured) {
            IsFeatured = isFeatured;
        }

        public void setSubCategoryid(Integer subCategoryid) {
            SubCategoryid = subCategoryid;
        }

        public void setProduct_name_ar(String product_name_ar) {
            this.product_name_ar = product_name_ar;
        }

        public void setProduct_name_en(String product_name_en) {
            this.product_name_en = product_name_en;
        }

        public void setPrice(Integer price) {
            Price = price;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setCategory_name_ar(String category_name_ar) {
            this.category_name_ar = category_name_ar;
        }

        public void setCategory_name_en(String category_name_en) {
            this.category_name_en = category_name_en;
        }

        public void setSub_category_name_en(String sub_category_name_en) {
            this.sub_category_name_en = sub_category_name_en;
        }

        public void setSub_category_name_ar(String sub_category_name_ar) {
            this.sub_category_name_ar = sub_category_name_ar;
        }

        public void setWishlists(Integer wishlists) {
            this.wishlists = wishlists;
        }

        public void setCart(Integer cart) {
            this.cart = cart;
        }

        public Integer getCompare_id() {
            return Compare_id;
        }

        public Integer getProduct_id() {
            return Product_id;
        }

        public Integer getCategoryid() {
            return Categoryid;
        }

        public Integer getIsFeatured() {
            return IsFeatured;
        }

        public Integer getSubCategoryid() {
            return SubCategoryid;
        }

        public String getProduct_name_ar() {
            return product_name_ar;
        }

        public String getProduct_name_en() {
            return product_name_en;
        }

        public Integer getPrice() {
            return Price;
        }

        public String getPhoto() {
            return photo;
        }

        public String getCategory_name_ar() {
            return category_name_ar;
        }

        public String getCategory_name_en() {
            return category_name_en;
        }

        public String getSub_category_name_en() {
            return sub_category_name_en;
        }

        public String getSub_category_name_ar() {
            return sub_category_name_ar;
        }

        public Integer getWishlists() {
            return wishlists;
        }

        public Integer getCart() {
            return cart;
        }
    }
}
