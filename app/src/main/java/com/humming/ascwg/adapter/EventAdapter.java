package com.humming.ascwg.adapter;

import android.widget.ImageView;

import com.humming.ascwg.R;
import com.squareup.picasso.Picasso;
import com.wg.event.dto.EventListResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/5.
 */
public class EventAdapter extends BaseAdapter<EventListResponse> {

    public EventAdapter(List<EventListResponse> list) {
        super(R.layout.item_surprise_event, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, EventListResponse item) {
        helper.setText(R.id.item_surprise__title, item.getName())
                .setText(R.id.item_surprise__content, item.getTitle());
        Picasso.with(helper.getConvertView().getContext()).load(item.getBgImage()).into((ImageView) helper.getView(R.id.item_surprise__image));
    }

}
