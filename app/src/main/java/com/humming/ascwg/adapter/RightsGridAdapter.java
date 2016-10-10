package com.humming.ascwg.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wg.rights.dto.RightsDetailResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/19.
 * 权益Grid
 */
public class RightsGridAdapter extends BaseAdapter<RightsDetailResponse> {

    public RightsGridAdapter(List<RightsDetailResponse> data) {
        super(R.layout.item_rights_grid, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RightsDetailResponse item) {
        Log.v("xxxx","----"+item.getDescImage());
        helper.setText(R.id.item_rights_list__title, item.getTitle()).setText(R.id.item_rights_list__name, item.getName());
        Picasso.with(Application.getInstance().getBaseContext()).load(item.getImageUrl()).into((ImageView) helper.getView(R.id.item_rights_list__image));

    }
}
