package com.humming.ascwg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.ascwg.Config;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.requestUtils.BaseInformation;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.squareup.okhttp.Request;
import com.wg.common.dto.BaseResponse;


/**
 * Created by Elvira on 2016/5/12.
 * 设置密码
 */
public class SetPasswordActivity extends AbstractActivity implements View.OnClickListener {

    private TextView nextTv;
    private ImageView backIv;
    private EditText setPwd;
    private EditText resetPwd;
    public static String IS_FORGET = "is_forget";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        nextTv = (TextView) findViewById(R.id.content_setpwd__next);
        setPwd = (EditText) findViewById(R.id.content_setpwd__password);
        resetPwd = (EditText) findViewById(R.id.content_setpwd__reset_password);
        //  backIv = (ImageView) findViewById(R.id.content_code__back);
        //   backIv.setOnClickListener(this);
        nextTv.setEnabled(false);
        setPwd.addTextChangedListener(textListener);
        resetPwd.addTextChangedListener(textListener);
    }

    TextWatcher textListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String pwd = setPwd.getText().toString().trim();
            String resetpwd = resetPwd.getText().toString().trim();
            if (!"".equals(pwd) && !"".equals(resetpwd)) {
                nextTv.setEnabled(true);
                nextTv.setBackgroundResource(R.drawable.border_white);
                nextTv.setOnClickListener(SetPasswordActivity.this);

            }

        }
    };

    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.content_setpwd__next://下一步
                String pwd = setPwd.getText().toString().trim();
                String resetpwd = resetPwd.getText().toString().trim();
                if (pwd.length() > 12) {
                    setPwd.setError("密码过长");
                } else if (pwd.length() < 6) {
                    setPwd.setError("密码过短");
                } else if (!pwd.equals(resetpwd)) {
                    resetPwd.setError("两次密码输入不一致");
                } else {
                    //  mLoading.show();
                    setPassword(pwd);
                }
                break;
            //    case R.id.content_code__back:
            //      finish();
            //    break;
        }
    }

    private void setPassword(String pwd) {
        BaseInformation getVerification = new BaseInformation();
        getVerification.setPwd(pwd);

        OkHttpClientManager.postAsyn(Config.SET_PASSWORD, new OkHttpClientManager.ResultCallback<BaseResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(BaseResponse response) {
                if ("1".equals(response.getStatus() + "")) {
                    //   mLoading.hide();
                    if ("isforget".equals(getIntent().getStringExtra(IS_FORGET))) {
                        startActivity(new Intent(SetPasswordActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SetPasswordActivity.this, SetInformationActivity.class));
                    }
                } else {
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, getVerification, BaseResponse.class);
    }
}
