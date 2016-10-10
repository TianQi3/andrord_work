package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/12.
 */
public class WinesDetails implements IRequestMainData {
    private long itemId;
    private String language;

    public WinesDetails() {
    }

    public WinesDetails(long itemId, String language) {
        this.itemId = itemId;
        this.language = language;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
