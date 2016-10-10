package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.ascwg.R;
import com.squareup.picasso.Picasso;
import com.wg.item.dto.ItemListResponse;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class WinesListAdapter extends RecyclerView.Adapter<WinesListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemListResponse> mList;
    private LayoutInflater mInflater;


    public WinesListAdapter(Context context, List<ItemListResponse> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public WinesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_wines, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WinesListAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        ItemListResponse listResponse = mList.get(position);
        // holder.image.setImageResource(R.drawable.wines_img);
        if (listResponse.getImageUrl().isEmpty()) {
            holder.image.setImageResource(R.drawable.wine);
        } else {
            Picasso.with(mContext).load(listResponse.getImageUrl()).error(R.drawable.wines_img).into(holder.image);
        }
        holder.nameCn.setText(listResponse.getNameCn());
        holder.nameEn.setText(listResponse.getNameEn());
        if ("0".equals(listResponse.getCostPrice())) {
            holder.priceItem.setVisibility(View.GONE);
        } else {
            holder.priceItem.setVisibility(View.VISIBLE);
            holder.price.setText("¥" + listResponse.getCostPrice());
        }
        holder.vintage.setText(listResponse.getVintage());
        if (mOnItemClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(vh.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View priceItem;
        public ImageView image;
        public TextView nameCn;
        public TextView nameEn;
        public TextView price;
        public TextView vintage;


        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView
                    .findViewById(R.id.list_item_wines__image);
            nameCn = (TextView) itemView
                    .findViewById(R.id.list_item_wines__name_cn);
            nameEn = (TextView) itemView
                    .findViewById(R.id.list_item_wines__name_en);
            price = (TextView) itemView
                    .findViewById(R.id.list_item_wines__price);
            priceItem = itemView
                    .findViewById(R.id.list_item_wines__price_item);
            vintage = (TextView) itemView
                    .findViewById(R.id.list_item_wines__vintage);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
