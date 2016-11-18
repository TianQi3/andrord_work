package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/16.
 */
public class OrderDetailRequest implements IRequestMainData {
    private long id;
    private String orderNo;

    public OrderDetailRequest() {
    }

    public OrderDetailRequest(long id, String orderNo) {
        this.id = id;
        this.orderNo = orderNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}

