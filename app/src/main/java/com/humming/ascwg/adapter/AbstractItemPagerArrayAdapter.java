package com.humming.ascwg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.humming.ascwg.R;
import com.humming.ascwg.component.ItemViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ztq on 1/9/16.
 */
public abstract class AbstractItemPagerArrayAdapter<TypeRowData, TypeViewHolder> extends ArrayAdapter {
    private int resource;
    private int[] itemPageResArray;
    private int defaultPageIndex = -1;
    private int size;

    private List<ViewPagerState> viewPagerStates = new ArrayList<>();
    protected static int viewPagerHolderKey = R.string.item_view_pager_holder;


    public AbstractItemPagerArrayAdapter(Context context, int resource, List<TypeRowData> items, int[] itemPageResArray, int defaultPageIndex, String
            types) {
        super(context, resource, items);
        this.resource = resource;
        this.defaultPageIndex = defaultPageIndex;
        this.itemPageResArray = itemPageResArray;
        size = items.size();
        ViewPagerState viewPagerState;
        for (int i = 0; i < size; i++) {
            viewPagerState = new ViewPagerState();
            viewPagerState.setCurrentIndex(defaultPageIndex);
            viewPagerStates.add(viewPagerState);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup row = (ViewGroup) convertView;
        ViewPagerHolder viewPagerHolder = null;
        ItemViewPager viewPager = null;
        TypeViewHolder viewHolder;
        ViewPagerState viewPagerState = viewPagerStates.get(position);
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (ViewGroup) inflater.inflate(resource, parent, false);
            viewHolder = createViewHolder();
            viewPagerHolder = new ViewPagerHolder();
            viewPager = viewPagerHolder.viewPager = (ItemViewPager) inflater.inflate(R.layout.list_item_view_pager, parent, false);
            row.addView(viewPager);
            int length = itemPageResArray.length;
            List<View> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                View view = inflater.inflate(itemPageResArray[i], parent, false);
                list.add(view);
            }
            initViewHolder(position, viewHolder, list);

            PagerAdapter adapter = createItemViewPagerAdapter(list);

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(viewPagerState.getCurrentIndex());
            viewPager.setOnListItemPageChangedListener(new ItemViewPager.OnListItemPageChangedListener() {
                @Override
                public void onPageChange(int listPosition, int itemPagePosition) {

                    if(itemPagePosition==0){
                        ViewPagerState viewPagerState = viewPagerStates.get(listPosition);
                        viewPagerState.setCurrentIndex(itemPagePosition);
                    }else{
                        for(int i =0;i<size;i++){
                            ViewPagerState viewPagerState = viewPagerStates.get(i);
                            viewPagerState.setCurrentIndex(0);
                            viewPagerState.setSmoothScroll(true);
                            notifyDataSetChanged();
                        }
                        ViewPagerState viewPagerState = viewPagerStates.get(listPosition);
                        viewPagerState.setCurrentIndex(itemPagePosition);
                        viewPagerState.setSmoothScroll(false);
                        notifyDataSetChanged();
                    }

                }
            });
            row.setTag(viewHolder);

            row.setTag(viewPagerHolderKey, viewPagerHolder);
        } else {
            viewHolder = (TypeViewHolder) row.getTag();
            viewPagerHolder = (ViewPagerHolder) row.getTag(viewPagerHolderKey);
        }
        viewPager = viewPagerHolder.viewPager;

        setItemData(position, viewHolder, (TypeRowData) getItem(position));

        viewPager.setCurrentItem(position, viewPagerState.getCurrentIndex(), viewPagerState.isSmoothScroll());
        return row;
    }

    protected com.humming.ascwg.adapter.ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
        return new ItemViewPagerAdapter(list);
    }

    protected abstract TypeViewHolder createViewHolder();

    protected abstract void initViewHolder(int position, TypeViewHolder viewHolder, List<View> itemPages);

    protected abstract void setItemData(int position, TypeViewHolder viewHolder, TypeRowData itemData);

    protected class ViewPagerHolder {
        ItemViewPager viewPager;
    }

    public void updateViewPagerState(int position, int index, boolean smoothScroll) {
        ViewPagerState viewPagerState = viewPagerStates.get(position);
        viewPagerState.setCurrentIndex(index);
        viewPagerState.setSmoothScroll(smoothScroll);
    }

    protected class ViewPagerState {
        private int currentIndex;
        private boolean smoothScroll = false;

        public int getCurrentIndex() {
            return currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        public boolean isSmoothScroll() {
            return smoothScroll;
        }

        public void setSmoothScroll(boolean smoothScroll) {
            this.smoothScroll = smoothScroll;
        }
    }
}

