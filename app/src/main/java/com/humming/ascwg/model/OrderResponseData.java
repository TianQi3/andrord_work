package com.humming.ascwg.model;

/**
 * Created by Zhtq on 16/11/11.
 */

public class OrderResponseData {
    private int orderStatus;

    public OrderResponseData() {
    }

    public OrderResponseData(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
