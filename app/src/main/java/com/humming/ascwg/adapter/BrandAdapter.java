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
import com.wg.baseinfo.dto.BrandResponse;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {

    private Context mContext;
    private List<BrandResponse> mList;
    private LayoutInflater mInflater;


    public BrandAdapter(Context context, List<BrandResponse> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BrandAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_brand, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BrandAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        BrandResponse brandResponse = mList.get(position);
        holder.mBrandCn.setText(brandResponse.getNameCn());
        holder.mBrandEn.setText(brandResponse.getNameEn());
       // holder.mBrandimage.setImageResource(R.drawable.brand_img);
        Picasso.with(mContext).load(brandResponse.getPicPath()).error(R.drawable.brand_img).into(holder.mBrandimage);
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
        public ImageView mBrandimage;
        public TextView mBrandEn;
        public TextView mBrandCn;


        public MyViewHolder(View itemView) {
            super(itemView);

            mBrandimage = (ImageView) itemView
                    .findViewById(R.id.list_item_brand__image);
            mBrandCn = (TextView) itemView
                    .findViewById(R.id.list_item_brand__cn);
            mBrandEn = (TextView) itemView
                    .findViewById(R.id.list_item_brand__en);
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
