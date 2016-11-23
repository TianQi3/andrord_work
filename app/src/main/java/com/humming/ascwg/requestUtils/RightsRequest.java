package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/16.
 */
public class RightsRequest implements IRequestMainData {
    private int page;

    public RightsRequest() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
