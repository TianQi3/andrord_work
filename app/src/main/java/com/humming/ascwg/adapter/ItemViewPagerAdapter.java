package com.humming.ascwg.adapter;

/**
 * Created by ztq on 8/11/16.
 */

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ItemViewPagerAdapter extends PagerAdapter {
    private final List<View> listView;

    public ItemViewPagerAdapter(List<View> viewList) {
        super();
        this.listView = viewList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = listView.get(position);
        collection.addView(view, 0);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) listView.get(position));
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
