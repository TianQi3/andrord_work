package com.humming.ascwg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;

import java.util.Locale;

/**
 * Created by Zhtq on 2016/5/12.
 * 设置用户信息
 */
public class SettingActivity extends AbstractActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title;
    private LinearLayout updatePasswordLayout, bindWechatlayout, updateLanguagelayout;
    private TextView ExitTv, languageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.action_settings));
        ExitTv = (TextView) findViewById(R.id.activity_setting__exit);
        languageTv = (TextView) findViewById(R.id.activity_setting__update_language);
        Locale curLocal = getResources().getConfiguration().locale;
        if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
            languageTv.setText(getResources().getString(R.string.chinese));
        } else {
            languageTv.setText(getResources().getString(R.string.english));
        }
        updateLanguagelayout = (LinearLayout) findViewById(R.id.activity_setting__update_language_layout);
        updatePasswordLayout = (LinearLayout) findViewById(R.id.activity_setting__password_layout);
        bindWechatlayout = (LinearLayout) findViewById(R.id.activity_setting__bind_weChat_layout);

        back.setOnClickListener(this);
        ExitTv.setOnClickListener(this);
        updatePasswordLayout.setOnClickListener(this);
        updateLanguagelayout.setOnClickListener(this);
        bindWechatlayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_setting__exit://退出登录
                Application.getInstance().finishAllActivity();
                Intent intents = new Intent(Application.getInstance().getApplicationContext(), LoginActivity.class);
                intents.putExtra(LoginActivity.LOGIN_FLAG, "1");
                Application.getInstance().getCurrentActivity().startActivity(intents);
                break;
            case R.id.activity_setting__bind_weChat_layout://绑定微信
                break;
            case R.id.activity_setting__update_language_layout://修改语言
                startActivity(new Intent(Application.getInstance().getCurrentActivity(), ChangeLanguageActivity.class));
                break;
            case R.id.activity_setting__password_layout://更改密码
                startActivity(new Intent(Application.getInstance().getCurrentActivity(), UpdatePasswordActivity.class));
                finish();
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }
}

