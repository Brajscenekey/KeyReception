package com.key.keyreception.activity.model;

/**
 * Created by Ravi Birla on 29,August,2019
 */
public class CardData {
    private String product_id;
    private String quantity;

    public CardData(String product_id, String product_price, String quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.product_price = product_price;
    }

    private String product_price;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
