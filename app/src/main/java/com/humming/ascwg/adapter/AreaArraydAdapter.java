package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humming.ascwg.R;
import com.wg.baseinfo.dto.ProvinceResponse;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class AreaArraydAdapter extends RecyclerView.Adapter<AreaArraydAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProvinceResponse> mList;
    private LayoutInflater mInflater;


    public AreaArraydAdapter(Context context, List<ProvinceResponse> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AreaArraydAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_text, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AreaArraydAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        ProvinceResponse provinceResponse = mList.get(position);
        holder.area.setText(provinceResponse.getProvince().toString());

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
        TextView area;

        public MyViewHolder(View itemView) {
            super(itemView);

            area = (TextView) itemView
                    .findViewById(R.id.list_item_text__content);
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
