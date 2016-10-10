package com.humming.ascwg.model;

/**
 * Created by Zhtq on 16/8/15.
 */
public class OrderSelect {
    private long shoppingCartId;
    private String nameEn;
    private String nameCn;
    private String vintage;
    private float costPrice;
    private String imageUrl;
    private int quantity;
    private boolean select;

    public OrderSelect() {
    }

    public OrderSelect(long shoppingCartId, String nameEn, String nameCn, String vintage, float costPrice, String imageUrl, int quantity, boolean select) {
        this.shoppingCartId = shoppingCartId;
        this.nameEn = nameEn;
        this.nameCn = nameCn;
        this.vintage = vintage;
        this.costPrice = costPrice;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.select = select;
    }

    public long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public float getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(float costPrice) {
        this.costPrice = costPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
