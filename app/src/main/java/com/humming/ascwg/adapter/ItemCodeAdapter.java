package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humming.ascwg.R;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class ItemCodeAdapter extends RecyclerView.Adapter<ItemCodeAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater mInflater;


    public ItemCodeAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ItemCodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_text, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemCodeAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        holder.content.setText(mList.get(position));
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
        public TextView content;


        public MyViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView
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
