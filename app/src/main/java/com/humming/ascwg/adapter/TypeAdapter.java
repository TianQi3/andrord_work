package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humming.ascwg.R;
import com.wg.baseinfo.dto.ItemTypeResponse;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemTypeResponse> mList;
    private LayoutInflater mInflater;


    public TypeAdapter(Context context, List<ItemTypeResponse> data) {
        this.mContext = context;
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_wines_type, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TypeAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        ItemTypeResponse typeResponse = mList.get(position);
        holder.mTypeCn.setText(typeResponse.getNameCn());
        holder.mTypeEn.setText(typeResponse.getNameEn());
        if (mOnItemClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(vh.itemView, position);
                }
            });
        }/**/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTypeEn;
        public TextView mTypeCn;


        public MyViewHolder(View itemView) {
            super(itemView);
            mTypeCn = (TextView) itemView
                    .findViewById(R.id.list_item_wines_type__cn);
            mTypeEn = (TextView) itemView
                    .findViewById(R.id.list_item_wines_type__en);
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
