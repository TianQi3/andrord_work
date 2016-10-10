package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/16.
 */
public class OrderDetailRequest implements IRequestMainData {
    private long id;

    public OrderDetailRequest() {
    }

    public OrderDetailRequest(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

