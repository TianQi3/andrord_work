package com.humming.ascwg.adapter;

import android.widget.ImageView;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;
import com.squareup.picasso.Picasso;
import com.wg.item.dto.YumItemListResponse;

import java.util.List;

/**
 * Created by Zhtq on 2016/8/5.
 */
public class RightsDetailAdapter extends BaseAdapter<YumItemListResponse> {

    private List<YumItemListResponse> responses;

    public RightsDetailAdapter(List<YumItemListResponse> list) {
        super(R.layout.list_item_wines, list);
        this.responses = list;
    }


    @Override
    protected void convert(BaseViewHolder helper, final YumItemListResponse item, final int position) {
        helper.setText(R.id.list_item_wines__name_cn, item.getNameCn())
                .setText(R.id.list_item_wines__name_en, item.getNameEn())
                .setText(R.id.list_item_wines__price, item.getCostPrice() + "")
                .setText(R.id.list_item_wines__vintage, item.getVintage());
        ImageView imageView = helper.getView(R.id.list_item_wines__image);
        Picasso.with(Application.getInstance().getBaseContext()).load(item.getImageUrl()).into(imageView);
    }

}