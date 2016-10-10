package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.R;
import com.squareup.picasso.Picasso;
import com.wg.baseinfo.dto.CountryResponse;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    private Context mContext;
    private List<CountryResponse> mList;
    private LayoutInflater mInflater;


    public CountryAdapter(Context context, List<CountryResponse> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_country, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CountryAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        CountryResponse countryResponse = mList.get(position);
        holder.mCountryCn.setText(countryResponse.getNameCn());
        holder.mCountryEn.setText(countryResponse.getNameEn());
        Picasso.with(mContext).load(countryResponse.getPicPath()).error(R.drawable.asc_logo).into(holder.mCountryimage);
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
        public ImageView mCountryimage;
        public TextView mCountryEn;
        public TextView mCountryCn;
        public LinearLayout out;


        public MyViewHolder(View itemView) {
            super(itemView);

            mCountryimage = (ImageView) itemView
                    .findViewById(R.id.list_item_country__image);
            mCountryCn = (TextView) itemView
                    .findViewById(R.id.list_item_country__cn);
            mCountryEn = (TextView) itemView
                    .findViewById(R.id.list_item_country__en);
            out = (LinearLayout) itemView.findViewById(R.id.out);
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
