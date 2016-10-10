package com.humming.ascwg.adapter;

import com.humming.ascwg.R;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/16.
 * 促销详情
 */
public class PromotionDetailsAdapter extends BaseAdapter<Map<String,String>> {
    public PromotionDetailsAdapter(List<Map<String, String>> data) {
        super(R.layout.item_surprise_promotion_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, String> item) {

    }
}
