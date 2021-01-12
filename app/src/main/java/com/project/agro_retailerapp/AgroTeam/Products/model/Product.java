package com.project.agro_retailerapp.AgroTeam.Products.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("value")
    private String value;
    @SerializedName("image")
    private String image;

    @SerializedName("stock")
    private String stock;


    @SerializedName("agro_cell")
    private String agro_cell;


    @SerializedName("description")
    private String description;


    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }


    public String getDescription() {
        return description;
    }

    public String getAgroCell() {
        return agro_cell;
    }

    public String getProductId() {
        return product_id;
    }


}