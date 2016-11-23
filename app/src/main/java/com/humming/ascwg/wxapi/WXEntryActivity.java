package com.humming.ascwg.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.LoginActivity;
import com.humming.ascwg.activity.WxCheckPhoneActivity;
import com.humming.ascwg.requestUtils.WxUserInfoRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.wg.user.dto.LoginUserInfoResponse;

import java.io.IOException;

public class WXEntryActivity extends AbstractActivity implements IWXAPIEventHandler {
    private Bundle bundle;
    private OkHttpClient okHttpClient;
    private Handler mDelivery;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivity.msgApi.handleIntent(getIntent(), WXEntryActivity.this);  //必须调用此句话
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LoginActivity.msgApi.handleIntent(intent, WXEntryActivity.this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq req) {
        System.out.println("9999999");
    }

    /**
     * Title: onResp
     * <p>
     * API：https://open.weixin.qq.com/ cgi- bin/showdocument ?action=dir_list&t=resource/res_list&verify=1&id=open1419317853 &lang=zh_CN
     * Description:在此处得到Code之后调https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * 获取到token和openID。之后再调用https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID 获取用户个人信息
     *
     * @param arg0
     * @see com.tencent.mm.sdk.openapi.IWXAPIEventHandler#
     */
    @Override
    public void onResp(BaseResp arg0) {
        bundle = getIntent().getExtras();
        SendAuth.Resp resp = new SendAuth.Resp(bundle);
        okHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
        //获取到code之后，需要调用接口获取到access_token
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            String code = resp.code;
            Request request = new Request.Builder().url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.APP_ID + "&secret=" + Constant.APP_SECRERT + "&code=" + code + "&grant_type=authorization_code").build();
            //获取微信access_token
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.v("xxxxxx", "xxxxxx");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    final String string = response.body().string();
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    String access_token = node.get("access_token").toString();
                    String accessToken = access_token.substring(1, access_token.length() - 1);
                    String openid = node.get("openid").toString();
                    String openId = openid.substring(1, openid.length() - 1);
                    String refresh_token = node.get("refresh_token").toString();
                    String refreshTokens = refresh_token.substring(1, refresh_token.length() - 1);
                    CheckLatest(accessToken, openId, refreshTokens);
                    //refreshToken(refreshTokens);
                }
            });

        } else {
            WXEntryActivity.this.finish();
        }
    }

    //检查是access——token是否失效
    public void CheckLatest(final String access_token, final String openid, final String refresh_token) {
        Request request = new Request.Builder().url("https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("xxxxxx", "xxxxxx");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final String string = response.body().string();
                JsonNode node = mapper.readValue(string, JsonNode.class);
                String errmsg = node.get("errmsg").toString();
                String errMsg = errmsg.substring(1, errmsg.length() - 1);
                if ("ok".equals(errMsg)) {//token有效
                    getWxPersonMessage(access_token, openid);
                } else {//无效
                    refreshToken(refresh_token);
                }
            }
        });
    }

    //刷新token
    public void refreshToken(String refresh_token) {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + Constant.APP_ID + "&refresh_token=" + refresh_token + "&grant_type=refresh_token";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("xxxxxx", "xxxxxx");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final String string = response.body().string();
                JsonNode node = mapper.readValue(string, JsonNode.class);
                String access_token = node.get("access_token").toString();
                String openid = node.get("openid").toString();
                String accessToken = access_token.substring(1, access_token.length() - 1);
                String openId = access_token.substring(1, openid.length() - 1);
                getWxPersonMessage(accessToken, openId);
            }
        });
    }

    //获取微信个人信息
    public void getWxPersonMessage(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("xxxxxx", "xxxxxx");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final String string = response.body().string();
                Log.v("xxxxxx", "string:" + string);
                JsonNode node = mapper.readValue(string, JsonNode.class);
                String nickName = node.get("nickname").toString();
                String headimgurl = node.get("headimgurl").toString();
                String nickNames = nickName.substring(1, nickName.length() - 1);
                String headImage = headimgurl.substring(1, headimgurl.length() - 1);
                String openid = node.get("openid").toString().substring(1, node.get("openid").toString().length() - 1);
                String sex = node.get("sex").toString();
                String province = node.get("province").toString().substring(1, node.get("province").toString().length() - 1);
                String city = node.get("city").toString().substring(1, node.get("city").toString().length() - 1);
                String country = node.get("country").toString().substring(1, node.get("country").toString().length() - 1);
                String unionid = node.get("unionid").toString().substring(1, node.get("unionid").toString().length() - 1);
                String language = node.get("language").toString().substring(1, node.get("language").toString().length() - 1);
                final WxUserInfoRequest wxUserInfoRequest = new WxUserInfoRequest();
                wxUserInfoRequest.setNickname(nickNames);
                wxUserInfoRequest.setHeadimgurl(headImage);
                wxUserInfoRequest.setOpenid(openid);
                wxUserInfoRequest.setSex(sex);
                wxUserInfoRequest.setProvince(province);
                wxUserInfoRequest.setCity(city);
                wxUserInfoRequest.setCountry(country);
                wxUserInfoRequest.setUnionid(unionid);
                wxUserInfoRequest.setLanguage(language);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.HEAD_IMAGE, headImage, WXEntryActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.CONTRACT, nickNames, WXEntryActivity.this);
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClientManager.postAsyn(Config.WXLOGIN, new OkHttpClientManager.ResultCallback<LoginUserInfoResponse>() {

                            @Override
                            public void onError(Request request, Error info) {
                                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                                showShortToast(info.getInfo());
                            }

                            @Override
                            public void onResponse(LoginUserInfoResponse response) {
                                WXEntryActivity.this.finish();
                                SharePrefUtil.putString(Constant.FILE_NAME, "thirdInfoId", response.getThirdInfoId() + "", WXEntryActivity.this);
                                SharePrefUtil.putString(Constant.FILE_NAME, "thirdInfoType", response.getThirdInfoType(), WXEntryActivity.this);
                                if (response.getIsNeedRegister() == 1) {//需要验证手机号
                                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), WxCheckPhoneActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                                /**/
                            }

                            @Override
                            public void onOtherError(Request request, Exception exception) {
                                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                            }
                        }, wxUserInfoRequest, LoginUserInfoResponse.class);

                    }
                });

            }
        });
    }
}