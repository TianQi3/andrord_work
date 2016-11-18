package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/11.
 */
public class ShoppingCartRequest implements IRequestMainData {
    private long shoppingCartId;
    private long itemId;
    private int quantity;
    private int scType;

    public ShoppingCartRequest() {
    }

    public ShoppingCartRequest(long shoppingCartId, long itemId, int quantity, int scType) {
        this.shoppingCartId = shoppingCartId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.scType = scType;
    }

    public long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getScType() {
        return scType;
    }

    public void setScType(int scType) {
        this.scType = scType;
    }
}
