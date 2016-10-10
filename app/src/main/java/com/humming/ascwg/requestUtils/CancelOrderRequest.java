package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/16.
 */
public class CancelOrderRequest implements IRequestMainData {
    private long orderId;

    public CancelOrderRequest() {
    }

    public CancelOrderRequest(long id) {
        this.orderId = id;
    }

    public long getId() {
        return orderId;
    }

    public void setId(long id) {
        this.orderId = id;
    }
}

