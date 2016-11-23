package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.ascwg.R;
import com.humming.ascwg.model.OrderSelect;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderSelect> mList;
    private LayoutInflater mInflater;


    public SettlementAdapter(Context context, List<OrderSelect> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SettlementAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_order_, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SettlementAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        OrderSelect orderSelect = mList.get(position);
        Picasso.with(mContext).load(orderSelect.getImageUrl()).into(holder.mimage);
        holder.mName.setText(orderSelect.getNameCn() + orderSelect.getNameEn());
        holder.mNumber.setText("×" + orderSelect.getQuantity() + "");
        holder.mPrice.setText("¥" + orderSelect.getCostPrice() + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mimage;
        public TextView mName;
        public TextView mPrice;
        public TextView mNumber;


        public MyViewHolder(View itemView) {
            super(itemView);

            mimage = (ImageView) itemView
                    .findViewById(R.id.item_my_order__wine_image);
            mName = (TextView) itemView
                    .findViewById(R.id.item_my_order__wine_name);
            mPrice = (TextView) itemView
                    .findViewById(R.id.item_my_order__wine_price);
            mNumber = (TextView) itemView
                    .findViewById(R.id.item_my_order__wine_num);
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
