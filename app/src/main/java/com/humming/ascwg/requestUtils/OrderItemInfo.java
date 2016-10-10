package com.humming.ascwg.requestUtils;

/**
 * Created by Zhtq on 16/8/16.
 */
public class OrderItemInfo {
    private long shoppingCartId;
    private int quantity;

    public OrderItemInfo() {
    }

    public OrderItemInfo(long shoppingCartId, int quantity) {
        this.shoppingCartId = shoppingCartId;
        this.quantity = quantity;
    }

    public long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
