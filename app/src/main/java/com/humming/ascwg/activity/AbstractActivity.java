package com.humming.ascwg.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;
import com.humming.ascwg.component.Loading;

/**
 * Created by Zhtq on 16/8/3.
 * <p>
 * activity公共类
 */
public class AbstractActivity extends AppCompatActivity {
    protected Loading mLoading;
    protected InputMethodManager imm;

    @Override
    protected void onResume() {
        super.onResume();
        Application.getInstance().setCurrentActivity(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mLoading = (Loading) findViewById(R.id.activity_loading);
    }

    /**
     * 短暂显示Toast提示(来自String)
     */
    protected void showShortToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
