package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/18.
 */
public class EventPromotionRequest implements IRequestMainData {
    private long eventId;
    private long promotionId;
    private String remark;
    private String contact;
    private String phone;

    public EventPromotionRequest() {
    }

    public EventPromotionRequest(long eventId, long promotionId, String remark, String contact, String phone) {
        this.eventId = eventId;
        this.promotionId = promotionId;
        this.remark = remark;
        this.contact = contact;
        this.phone = phone;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
