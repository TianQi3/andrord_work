package com.humming.ascwg.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.model.OrderResponseData;
import com.humming.ascwg.requestUtils.OrderDetailRequest;
import com.humming.ascwg.requestUtils.PayContentRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.PayResult;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.humming.ascwg.activity.my.MyOrderDetailsActivity.orderHead;

/**
 * Created by Zhtq on 16/11/22.
 */

public class PayTypeSelectActivity extends AbstractActivity {
    private TextView OrderNo, orderAmount, title, pay;
    private RadioGroup aliPayGroup, wxPayGroup;
    public CheckBox aliPayBox;
    public CheckBox wxPayBox;
    public static String ORDER_NO = "order_no";
    public static String ORDER_AMOUNT = "order_amount";
    private PayReq req;
    private ImageView back;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private Timer timer;
    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(Application.getInstance().getResources().getString(R.string.select_payment));
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String orderNo = getIntent().getStringExtra(ORDER_NO);
        String orderAmounts = getIntent().getStringExtra(ORDER_AMOUNT);
        OrderNo = (TextView) findViewById(R.id.content_type_select__order_no);
        orderAmount = (TextView) findViewById(R.id.content_type_select__order_amount);
        OrderNo.setText(orderNo);
        orderAmount.setText(orderAmounts);
        pay = (TextView) findViewById(R.id.content_pay_select__pay);
        aliPayGroup = (RadioGroup) findViewById(R.id.content_pay_select__alipay_radiogroup);
        aliPayBox = (CheckBox) findViewById(R.id.content_pay_select__alipay_radio);
        wxPayBox = (CheckBox) findViewById(R.id.content_pay_select__wx_pay_radio);
        wxPayGroup = (RadioGroup) findViewById(R.id.content_pay_select__wx_pay_radiogroup);
        wxPayGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliPayBox.setChecked(false);
                wxPayBox.setChecked(true);
            }
        });
        aliPayGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliPayBox.setChecked(true);
                wxPayBox.setChecked(false);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wxPayBox.isChecked()) {//微信支付
                    PayContentRequest payContentRequest = new PayContentRequest();
                    payContentRequest.setPlatform(1);
                    payContentRequest.setOrderNo(orderNo);
                    OkHttpClientManager.postAsyn(Config.PAY_CONTENT, new OkHttpClientManager.ResultCallback<Map<String, Object>>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            showShortToast(info.getInfo());
                        }

                        @Override
                        public void onResponse(Map<String, Object> response) {
                            msgApi.registerApp(Constant.APP_ID);
                            req = new PayReq();
                            Map<String, Object> aa = (Map<String, Object>) response.get("data");
                            req.appId = Constant.APP_ID;
                            req.partnerId = (String) aa.get("partnerid");
                            req.prepayId = (String) aa.get("prepayid");
                            req.packageValue = "Sign=WXPay";
                            req.nonceStr = (String) aa.get("noncestr");
                            req.timeStamp = (String) aa.get("noncestr");
                            req.sign = (String) aa.get("timestamp");
                            msgApi.sendReq(req);
                            finish();
                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                        }
                    }, payContentRequest);
                } else if (aliPayBox.isChecked()) {//支付宝
                    PayContentRequest payContentRequest = new PayContentRequest();
                    payContentRequest.setPlatform(2);
                    payContentRequest.setOrderNo(orderNo);
                    OkHttpClientManager.postAsyn(Config.PAY_CONTENT, new OkHttpClientManager.ResultCallback<Map<String, Object>>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            showShortToast(info.getInfo());
                        }

                        @Override
                        public void onResponse(Map<String, Object> response) {
                            final String payInfo = (String) response.get("data");
                            /*for (Map.Entry entry : response.entrySet()) {
                                System.out.println(entry.getKey() + ":" + entry.getValue());
                            }*/
                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(Application.getInstance().getCurrentActivity());
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(payInfo, true);

                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandlers.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                            finish();
                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                        }
                    }, payContentRequest);
                } else {
                    showShortToast(getResources().getString(R.string.select_payment));
                }
            }
        });

    }

    //支付宝支付处理结果
    @SuppressLint("HandlerLeak")
    private Handler mHandlers = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
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

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void getOrderStatus() {
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setOrderNo(orderHead.getOrderNo());
        OkHttpClientManager.postAsyn(Config.ORDER_STATUS, new OkHttpClientManager.ResultCallback<OrderResponseData>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(OrderResponseData response) {
                if (response.getOrderStatus() == 6) {
                    // Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                    builder.setTitle(R.string.app_tip);
                    builder.setMessage(getString(R.string.pay_result_callback_msg, "支付成功"));
                    builder.show();
                    if (timer != null) {
                        timer.cancel();
                        // 一定设置为null，否则定时器不会被回收
                        timer = null;
                    }
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
}
