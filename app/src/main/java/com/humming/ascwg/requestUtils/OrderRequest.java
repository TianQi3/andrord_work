package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

import java.util.List;

/**
 * Created by Zhtq on 16/8/16.
 */
public class OrderRequest implements IRequestMainData {
    private long shippingAddressId;
    private String remark;
    private List<OrderItemInfo> items;

    public OrderRequest() {
    }

    public OrderRequest(long shippingAddressId, String remark, List<OrderItemInfo> items) {
        this.shippingAddressId = shippingAddressId;
        this.remark = remark;
        this.items = items;
    }

    public long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public List<OrderItemInfo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemInfo> items) {
        this.items = items;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

