package com.humming.ascwg.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.R;


/**
 * Created by Ztq on 12/29/15.
 */
public class Loading extends LinearLayout{
    private  TextView mText;

    public Loading(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.content_loading_4_cd_layout, this);
        mText = (TextView)findViewById(R.id.activity_loading_content__text);
        hide();
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public void setText(int stringRes) {
        mText.setText(stringRes);
    }

    public void show(){
        setVisibility(View.VISIBLE);
    }

    public void hide(){
        setVisibility(View.GONE);
    }
}
