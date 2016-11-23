package com.humming.ascwg.service;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.google.gson.Gson;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by Elvira on 2016/8/4.
 * 网络请求
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    public static final MediaType JSON = MediaType.parse("application/json;CharSet=UTF-8");

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass) {
        getInstance()._postAsyn(url, callback, requestMainDataData, resultVOClass);
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final TypeReference typeReference) {
        getInstance()._postAsyn(url, callback, requestMainDataData, typeReference);
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData) {
        getInstance()._postAsyn(url, callback, requestMainDataData);
    }

    //异步请求 类(返回普通类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", Application.getInstance().getCurrentActivity());
        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        Locale curLocal = Application.getInstance().getResources().getConfiguration().locale;
        if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
            requestData.setLanguage("cn");
        } else {
            requestData.setLanguage("en");
        }
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        requestData.setAppVersion(Build.BRAND + "_" + Build.DISPLAY + "_" + Build.FINGERPRINT + "_" + Build.ID);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, resultVOClass);
    }

    //异步请求 类(返回集合类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final TypeReference typeReference) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", Application.getInstance().getCurrentActivity());
        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        Locale curLocal = Application.getInstance().getResources().getConfiguration().locale;
        if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
            requestData.setLanguage("cn");
        } else {
            requestData.setLanguage("en");
        }
        requestData.setAppVersion(Build.BRAND + "_" + Build.DISPLAY + "_" + Build.FINGERPRINT + "_" + Build.ID);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, typeReference);
    }

    //异步请求 Map(返回map---用于调用支付)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", Application.getInstance().getCurrentActivity());
        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        Locale curLocal = Application.getInstance().getResources().getConfiguration().locale;
        if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
            requestData.setLanguage("cn");
        } else {
            requestData.setLanguage("en");
        }
        requestData.setAppVersion(Build.BRAND + "_" + Build.DISPLAY + "_" + Build.FINGERPRINT + "_" + Build.ID);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request);
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }


    private <T> Request buildPostRequest(String url, RequestData requestData) {

        String json = new Gson().toJson(requestData);
        RequestBody requestBody = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        String small_url = url + "?";
//        for (Param param : params) {
//            builder.add(param.key, param.value);
//        }
        for (int i = 0; i < params.length; i++) {
            if (i == params.length - 1) {
                small_url = small_url + params[i].key + "=" + params[i].value;
            } else {
                small_url = small_url + params[i].key + "=" + params[i].value + "&";
            }
            builder.add(params[i].key, params[i].value);
        }
        RequestBody requestBody = builder.build();

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    public static class Param {
        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request, final Class<T> resultVOClass) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        if (resultVOClass == ResponseData.class) {//只判断状态(只确定返回是否成功)
                            ResponseData responseData = new ResponseData();
                            responseData.setStatusCode(0);
                            sendSuccessResultCallback(responseData, callback);
                        } else {
                            JsonNode responseNode = node.get("response");
                            T data = mapper.treeToValue(responseNode, resultVOClass);
                            sendSuccessResultCallback(data, callback);
                        }
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request, final TypeReference typeReference) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        T result = mapper.readValue(responseNode.toString(), typeReference);
                        sendSuccessResultCallback(result, callback);
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        Map<String, Object> resutltMap = mapper.readValue(responseNode.toString(), Map.class);
                        sendSuccessResultCallback(resutltMap, callback);
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onOtherError(request, e);
            }
        });
    }

    private void sendErrorStringCallback(final Request request, final Error e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }


    public static abstract class ResultCallback<T> {

        public abstract void onError(Request request, Error info);

        public abstract void onResponse(T response);

        public abstract void onOtherError(Request request, Exception exception);
    }


}