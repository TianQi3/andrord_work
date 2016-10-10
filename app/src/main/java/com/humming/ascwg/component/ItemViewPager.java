package com.humming.ascwg.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ztq on 8/11/16.
 */
public class ItemViewPager extends ViewPager {
    private int listPosition = 0;
    private NoPageTransformer noPageTransformer;
    private OnListItemPageChangedListener onListItemPageChangedListener;

    public ItemViewPager(Context context) {
        super(context);
        init();
    }

    public ItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
//        this.setKeepScreenOn(false);
    }

    private void init(){
        final OnPageChangeListener onPageChangedListenernew = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pagePosition) {
                if(onListItemPageChangedListener == null){
                    return;
                }
                onListItemPageChangedListener.onPageChange(listPosition,pagePosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        this.addOnPageChangeListener(onPageChangedListenernew);
        noPageTransformer = new NoPageTransformer();

    }

    public void enableScrollAnimation(boolean flag){
        if(flag){

        }else{
            setPageTransformer(false, new NoPageTransformer());
        }
    }

    public void setOnListItemPageChangedListener(OnListItemPageChangedListener onListItemPageChangedListener) {
        this.onListItemPageChangedListener = onListItemPageChangedListener;
    }

    public void setCurrentItem(int listPosition,int itemPagePosition,boolean smoothScroll) {
        this.listPosition = listPosition;
        super.setCurrentItem(itemPagePosition,smoothScroll);
    }

    public interface OnListItemPageChangedListener{
        public void onPageChange(int listPosition, int itemPagePosition);
    }

    private static class NoPageTransformer implements PageTransformer {
        public void transformPage(View view, float position) {
            if (position < 0) {
                view.setScrollX((int)((float)(view.getWidth()) * position));
            } else if (position > 0) {
                view.setScrollX(-(int) ((float) (view.getWidth()) * -position));
            } else {
                view.setScrollX(0);
            }
        }
    }
}
