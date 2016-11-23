package com.humming.ascwg.activity;

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
import com.humming.ascwg.R;
import com.humming.ascwg.requestUtils.BaseInformation;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.common.dto.BaseResponse;


/**
 * Created by Zhtq on 2016/5/12.
 * 设置密码
 */
public class UpdatePasswordActivity extends AbstractActivity implements View.OnClickListener {

    private TextView nextTv, title;
    private ImageView back;
    private EditText setNewPwd;
    private EditText resetNewPwd;
    private EditText oldPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.action_settings));
        nextTv = (TextView) findViewById(R.id.content_updatepwd__submit);
        setNewPwd = (EditText) findViewById(R.id.content_updatepwd__new_password);
        resetNewPwd = (EditText) findViewById(R.id.content_updatepwd__new_password_again);
        oldPwd = (EditText) findViewById(R.id.content_updatepwd__old_password);
        back.setOnClickListener(this);
        nextTv.setEnabled(false);
        setNewPwd.addTextChangedListener(textListener);
        resetNewPwd.addTextChangedListener(textListener);
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
            String pwd = setNewPwd.getText().toString().trim();
            String resetpwd = resetNewPwd.getText().toString().trim();
            if (!"".equals(pwd) && !"".equals(resetpwd)) {
                nextTv.setEnabled(true);
                nextTv.setBackgroundResource(R.drawable.border_yellow_radius);
                nextTv.setOnClickListener(UpdatePasswordActivity.this);

            }

        }
    };

    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.content_updatepwd__submit://提交
                String pwd = setNewPwd.getText().toString().trim();
                String resetpwd = resetNewPwd.getText().toString().trim();
                if (pwd.length() > 12) {
                    setNewPwd.setError("密码过长");
                } else if (pwd.length() < 6) {
                    setNewPwd.setError("密码过短");
                } else if (!pwd.equals(resetpwd)) {
                    setNewPwd.setError("两次密码输入不一致");
                } else {
                    //  mLoading.show();
                    updatePassword(pwd);
                }
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    private void updatePassword(String pwd) {
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
