package com.humming.ascwg.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.my.MyOrderDetailsActivity;
import com.humming.ascwg.model.OrderResponseData;
import com.humming.ascwg.requestUtils.OrderDetailRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Timer;
import java.util.TimerTask;


public class WXPayEntryActivity extends AbstractActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    private void getOrderStatus() {
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setOrderNo(MyOrderDetailsActivity.orderHead.getOrderNo());
        OkHttpClientManager.postAsyn(Config.ORDER_STATUS, new OkHttpClientManager.ResultCallback<OrderResponseData>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(OrderResponseData response) {
                if (response.getOrderStatus() == 6) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                    builder.setTitle(R.string.app_tip);
                    builder.setMessage(getString(R.string.pay_result_callback_msg, "支付成功"));
                    builder.show();
                    if (timer != null) {
                        timer.cancel();
                        // 一定设置为null，否则定时器不会被回收
                        timer = null;
                    }
                    // Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                } else if (response.getOrderStatus() == 7) {
                    //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_status_7), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                    builder.setTitle(R.string.app_tip);
                    builder.setMessage(getString(R.string.pay_result_callback_msg, getResources().getString(R.string.order_status_7)));
                    builder.show();
                    if (timer != null) {
                        timer.cancel();
                        // 一定设置为null，否则定时器不会被回收
                        timer = null;
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, orderDetailRequest, OrderResponseData.class);
    }

    @Override
    public void onResp(BaseResp resp) {
        //Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        getOrderStatus();
                    }
                    super.handleMessage(msg);
                }

                ;
            };
            timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    // 需要做的事:发送消息
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
            timer.schedule(task, 1000);
            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyOrderDetailsActivity.class);
            intent.putExtra(MyOrderDetailsActivity.ORDER_TYPE, "0");
            intent.putExtra(MyOrderDetailsActivity.ORDER_ID, MyOrderDetailsActivity.orderId);
            startActivityForResult(intent, MyOrderDetailsActivity.ORDER_CODE);
            finish();

        }
    }
}