package com.humming.ascwg.component;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by Elvira on 2016/5/10.
 */
public class ScrollView extends android.widget.ScrollView{
    public ScrollView(Context context) {
        super(context);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

}
