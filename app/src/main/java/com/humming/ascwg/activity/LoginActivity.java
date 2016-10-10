package com.humming.ascwg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.requestUtils.BaseInformation;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.SharePrefUtil;
import com.humming.ascwg.R;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wg.user.dto.LoginUserInfoResponse;
import com.humming.ascwg.service.Error;

/**
 * Created by Elvira on 2016/8/9.
 * 登陆
 */
public class LoginActivity extends AbstractActivity implements View.OnClickListener {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private long mExitTime; //退出时间
    private TextView mSignInButton;
    public static final String SETTING_INFOS = "setting_infos";
    public static final String NAME = "NAME";
    public static final String PASSWORD = "PASSWORD";

    private TextView creatLoginTv;
    private TextView forgetPwdTv;
    private ImageView back;
    private ImageView wXLogin;
    public static IWXAPI msgApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameView = (EditText) findViewById(R.id.content_login__username);
        mPasswordView = (EditText) findViewById(R.id.content_login__password);
        mSignInButton = (TextView) findViewById(R.id.content_login__login);
        mSignInButton.setOnClickListener(this);
        String userName = SharePrefUtil.getString(Constant.FILE_NAME, "userName", "", LoginActivity.this);
        String passWord = SharePrefUtil.getString(Constant.FILE_NAME, "password", "", LoginActivity.this);
        mUsernameView.setText(userName);
        mPasswordView.setText(passWord);
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {

        } else {
            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MainActivity.class);
            Application.getInstance().getCurrentActivity().startActivity(intent);
            finish();

        }
        msgApi = WXAPIFactory.createWXAPI(this, null);
        wXLogin = (ImageView) findViewById(R.id.content_login__weixin);
        creatLoginTv = (TextView) findViewById(R.id.content_login__create_account);
        creatLoginTv.setOnClickListener(this);
        forgetPwdTv = (TextView) findViewById(R.id.content_login__forget_pw);
        forgetPwdTv.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.content_register__back);
        back.setOnClickListener(this);
        wXLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.content_register__back://返回
                finish();
                break;
            case R.id.content_login__login://登录
                attemptLogin();
                break;
            case R.id.content_login__forget_pw://忘记密码
                intent.setClass(Application.getInstance().getCurrentActivity(), RegisterActivity.class);
                intent.putExtra("isForget", true);
                intent.putExtra("phone", mUsernameView.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.content_login__create_account://创建账户
                intent.setClass(Application.getInstance().getCurrentActivity(), RegisterActivity.class);
                intent.putExtra("isForget", false);
                startActivity(intent);

                break;
            case R.id.content_login__weixin://微信登录
                msgApi = WXAPIFactory.createWXAPI(LoginActivity.this, Constant.APP_ID, true);
                msgApi.registerApp(Constant.APP_ID);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                msgApi.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调
                break;
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Store values at the time of the login attempt.
        mLoading.show();
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isEmailValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_employee_id));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            BaseInformation baseInformation = new BaseInformation();
            baseInformation.setLoginName(username);
            baseInformation.setPassword(password);

            OkHttpClientManager.postAsyn(Config.LOGIN, new OkHttpClientManager.ResultCallback<LoginUserInfoResponse>() {
                @Override
                public void onError(Request request, Error info) {
                    Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                    showShortToast(info.getInfo());
                    showProgress(false);
                }

                @Override
                public void onResponse(LoginUserInfoResponse response) {
                    //   if ("1".equals(response.getAccountType() + "")) {
                    SharePrefUtil.putString(Constant.FILE_NAME, "userName", username, LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, "password", password, LoginActivity.this);

                    SharePrefUtil.putString(Constant.FILE_NAME, "token", response.getToken(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.PHONE, response.getPhone(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.CONTRACT, response.getName(), LoginActivity.this);
                    //此处直接跳转,后续需要判断正则
                    mLoading.hide();
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.HEAD_IMAGE, response.getHeadImg(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.DISCOUNT, response.getDiscount() + "", LoginActivity.this);
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MainActivity.class);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    finish();
                    //  } else {
                    //      showShortToast("" + response.getAccountType());
                    //  }
                }

                @Override
                public void onOtherError(Request request, Exception exception) {
                    Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                    showProgress(false);
                }
            }, baseInformation, LoginUserInfoResponse.class);
        }
    }


    private boolean isEmailValid(String email) {
//        return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void showProgress(final boolean show) {
        if (show) {
            mUsernameView.setEnabled(false);
            mPasswordView.setEnabled(false);
            mSignInButton.setEnabled(false);
            //       mLoading.show();
        } else {
            mUsernameView.setEnabled(true);
            mPasswordView.setEnabled(true);
            mSignInButton.setEnabled(true);
            //         mLoading.hide();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, Application.getInstance().getString(R.string.application_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
