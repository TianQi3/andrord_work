package com.humming.ascwg.adapter;

import com.humming.ascwg.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/16.
 * 活动详情
 */
public class EventDetailsAdapter extends BaseAdapter<Map<String,String>> {
    public EventDetailsAdapter(List<Map<String, String>> data) {
        super(R.layout.item_surprise_event_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, String> item, int position) {

    }
}
