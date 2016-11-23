package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/11/2.
 */

public class PayContentRequest implements IRequestMainData {
    /**
     * 微信(1),
     * 支付宝(2),
     * 快钱(3),
     * 联动U付(4);
     */
    private int platform;
    private String orderNo;

    public PayContentRequest() {
    }

    public PayContentRequest(int platform, String orderNo) {
        this.platform = platform;
        this.orderNo = orderNo;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
