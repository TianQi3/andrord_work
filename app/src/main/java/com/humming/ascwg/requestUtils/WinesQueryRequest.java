package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class WinesQueryRequest implements IRequestMainData {
    private int page;
    private String itemCode;
    private String itemName;
    private String brandId;
    private String countryId;
    private String itemTypeId;

    public WinesQueryRequest() {
    }

    public WinesQueryRequest(String itemName, int page, String itemCode, String brandId, String countryId, String itemTypeId) {
        this.itemName = itemName;
        this.page = page;
        this.itemCode = itemCode;
        this.brandId = brandId;
        this.countryId = countryId;
        this.itemTypeId = itemTypeId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
