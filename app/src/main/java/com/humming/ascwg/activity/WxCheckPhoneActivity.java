package com.humming.ascwg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.R;
import com.humming.ascwg.model.VerificationCode;
import com.humming.ascwg.requestUtils.BaseInformation;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.BaseTools;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Request;

/**
 * Created by Elvira on 2016/5/12.
 * 注册
 */
public class WxCheckPhoneActivity extends AbstractActivity implements View.OnClickListener {


    private TextView nextTv;
    private TextView userAgreementTv;
    private ImageView backIv;
    private TextView getCodeTv;
    private EditText phoneNumEt;
    private EditText setCodeEt;
    private TextView titleTv;
    private TextView accountPWLogin;
    private TextView tipTv;
    private LinearLayout agreementLayout;
    private boolean isPress = false;
    private TimeCount timeCount;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        timeCount = new TimeCount(60000, 1000);
        nextTv = (TextView) findViewById(R.id.content_register__next);
        userAgreementTv = (TextView) findViewById(R.id.content_register__user_agreement);
        backIv = (ImageView) findViewById(R.id.content_register__back);
        getCodeTv = (TextView) findViewById(R.id.content_register__get_code);
        phoneNumEt = (EditText) findViewById(R.id.content_register__phonenum);
        setCodeEt = (EditText) findViewById(R.id.content_register__edit_code);
        titleTv = (TextView) findViewById(R.id.content_register__title);
        // accountPWLogin = (TextView) findViewById(R.id.content_login__create_account);
        /*accountPWLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        //   tipTv = (TextView) findViewById(R.id.content_register__tip);
        //  agreementLayout = (LinearLayout) findViewById(R.id.content_register__agreement_layout);
        nextTv.setEnabled(false);
        backIv.setOnClickListener(this);
        userAgreementTv.setOnClickListener(this);
        getCodeTv.setOnClickListener(this);
        setCodeEt.addTextChangedListener(textListener);
        phoneNumEt.addTextChangedListener(textListener);
        //  agreementLayout.setVisibility(View.VISIBLE);
        titleTv.setText(getResources().getString(R.string.text_get_identifying_phone));
        //tipTv.setText(getResources().getString(R.string.text_welcome));
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
            String code = setCodeEt.getText().toString().trim();
            String phone = phoneNumEt.getText().toString().trim();
            if (!"".equals(code) && !"".equals(phone) && isPress) {
                nextTv.setEnabled(true);
                // nextTv.setBackgroundResource(R.drawable.bg_border_radius_orange);
                nextTv.setOnClickListener(WxCheckPhoneActivity.this);
            }

        }
    };

    @Override
    public void onClick(View v) {
        String phoneStr = phoneNumEt.getText().toString().trim();
        String code = setCodeEt.getText().toString().trim();
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.content_register__next://下一步
                checkVerificationCode(phoneStr, code);
                break;
            case R.id.content_register__user_agreement://用户使用协议
                break;
            case R.id.content_register__back:
                finish();
                break;
            case R.id.content_register__get_code://获取验证码
                getCodeTv.setEnabled(false);
                if (BaseTools.isMobileNo(phoneStr)) {
                    getCode(phoneStr);
                } else {
                    phoneNumEt.setError(getResources().getString(R.string.hint_correct_phone));
                }
                break;
        }
    }

    //获取验证码
    private void getCode(String phone) {
        BaseInformation getVerification = new BaseInformation();
        getVerification.setPhone(phone);
        OkHttpClientManager.postAsyn(Config.GET_REGISTER_CODE, new OkHttpClientManager.ResultCallback<VerificationCode>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                //  getCodeTv.setEnabled(true);
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(VerificationCode response) {
                Log.v("xxxx", "verificationCode:" + response.getVerifyCode());
                isPress = true;
                timeCount.start();
                showShortToast("verificationCode:" + response.getVerifyCode());
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                //    getCodeTv.setEnabled(true);
                Toast.makeText(Application.getInstance().getCurrentActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, getVerification, VerificationCode.class);

    }

    //校验验证码是否正确
    private void checkVerificationCode(String phone, String code) {
        BaseInformation getVerification = new BaseInformation();
        getVerification.setPhone(phone);
        getVerification.setVerifyCode(code);
        Long thirdInfoId = Long.parseLong(SharePrefUtil.getString(Constant.FILE_NAME, "thirdInfoId", "", WxCheckPhoneActivity.this));
        String thirdInfoType = SharePrefUtil.getString(Constant.FILE_NAME, "thirdInfoType", "", WxCheckPhoneActivity.this);
        getVerification.setThirdInfoId(thirdInfoId);
        getVerification.setThirdInfoType(thirdInfoType);
        OkHttpClientManager.postAsyn(Config.CHECK_VERIFICATION_CODE, new OkHttpClientManager.ResultCallback<VerificationCode>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                getCodeTv.setEnabled(true);
            }

            @Override
            public void onResponse(VerificationCode response) {
                // if ("1".equals(response.getAccountType())) {
                SharePrefUtil.putString(Constant.FILE_NAME, "token", response.getToken(), WxCheckPhoneActivity.this);
                startActivity(new Intent(WxCheckPhoneActivity.this, MainActivity.class));
                finish();
                //   } else {
                //        showShortToast("验证码错误，请重新获取验证码");
                //   }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                getCodeTv.setEnabled(true);
                showShortToast(exception.getMessage());
            }
        }, getVerification, VerificationCode.class);
    }

    //获取验证码倒计时
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            getCodeTv.setText(getResources().getString(R.string.reget_identifying_code));
            getCodeTv.setEnabled(true);
            getCodeTv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCodeTv.setClickable(false);
            getCodeTv.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
