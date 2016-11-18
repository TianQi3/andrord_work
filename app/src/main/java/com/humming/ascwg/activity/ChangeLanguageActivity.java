package com.humming.ascwg.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;

import java.util.Locale;


/**
 * Created by Zhtq on 2016/5/12.
 * 设置密码
 */
public class ChangeLanguageActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView back;
    private LinearLayout chineseLayout, englishLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.settings_language));
        chineseLayout = (LinearLayout) findViewById(R.id.activity_change_language__chinese);
        englishLayout = (LinearLayout) findViewById(R.id.activity_change_language__english);
        back.setOnClickListener(this);
        englishLayout.setOnClickListener(this);
        chineseLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.activity_change_language__chinese://
                Locale curLocal = getResources().getConfiguration().locale;
                Configuration config = getResources().getConfiguration();//获取系统的配置
                String currentLanguage = "";
                config.locale = Locale.SIMPLIFIED_CHINESE;//将语言更改为英文
                currentLanguage = "english";
                SharedPreferences preferences = Application.getInstance().getSharedPreferences("language", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("currentLanguage", currentLanguage);
                editor.commit();
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());//更新配置
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                intent.putExtra(LoginActivity.LOGIN_FLAG, "1");
                Application.getInstance().getCurrentActivity().startActivity(intent);
                Application.getInstance().finishAllActivity();
                break;
            case R.id.activity_change_language__english://
                Locale curLocals = getResources().getConfiguration().locale;
                Configuration configs = getResources().getConfiguration();//获取系统的配置
                String currentLanguages = "";
                configs.locale = Locale.ENGLISH;//将语言更改为英文
                currentLanguages = "chinese";
                SharedPreferences preferencess = Application.getInstance().getSharedPreferences("language", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editors = preferencess.edit();
                editors.putString("currentLanguage", currentLanguages);
                editors.commit();
                getResources().updateConfiguration(configs, getResources().getDisplayMetrics());//更新配置
                Intent intents = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                intents.putExtra(LoginActivity.LOGIN_FLAG, "1");
                Application.getInstance().getCurrentActivity().startActivity(intents);
                Application.getInstance().finishAllActivity();
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }
}
