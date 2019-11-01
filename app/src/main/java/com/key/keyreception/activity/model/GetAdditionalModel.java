package com.key.keyreception.activity.model;

/**
 * Created by Ravi Birla on 03,September,2019
 */
public class GetAdditionalModel {

    private String id;
    private String quantity;
    private String price;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GetAdditionalModel(String id, String quantity, String price, String title) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
