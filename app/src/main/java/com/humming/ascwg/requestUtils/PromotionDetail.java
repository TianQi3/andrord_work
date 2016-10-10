package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/29.
 */

public class PromotionDetail implements IRequestMainData {
    private long promotionId;

    public PromotionDetail(long promotionId) {
        this.promotionId = promotionId;
    }

    public PromotionDetail() {
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }
}
