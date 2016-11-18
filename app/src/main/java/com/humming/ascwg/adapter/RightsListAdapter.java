package com.humming.ascwg.adapter;

import android.widget.ImageView;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;
import com.squareup.picasso.Picasso;
import com.wg.rights.dto.RightsDetailResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/19.
 * 权益List
 */
public class RightsListAdapter extends BaseAdapter<RightsDetailResponse> {

    public RightsListAdapter(List<RightsDetailResponse> data) {
        super(R.layout.item_rights_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RightsDetailResponse item, int position) {
        helper.setText(R.id.item_rights_list__title, item.getTitle());
        Picasso.with(Application.getInstance().getBaseContext()).load(item.getImageUrl()).into((ImageView) helper.getView(R.id.item_rights_list__image));
    }
}
