package com.humming.ascwg.adapter;

import android.widget.ImageView;

import com.humming.ascwg.R;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wg.promotion.dto.PromotionListResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/5.
 * 促销
 */
public class PromotionAdapter extends BaseAdapter<PromotionListResponse> {

    public PromotionAdapter(List<PromotionListResponse> list) {
        super(R.layout.item_surprise_promotion, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, PromotionListResponse item) {
        helper.setText(R.id.item_surprise__title, item.getTitle())
                .setText(R.id.item_surprise__content, item.getName());
        Picasso.with(helper.getConvertView().getContext()).load(item.getBgImage()).into((ImageView) helper.getView(R.id.item_surprise__image));
    }
}
