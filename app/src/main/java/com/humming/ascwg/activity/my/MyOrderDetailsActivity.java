package com.humming.ascwg.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.content.ShoppingCartContent;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.CancelOrderRequest;
import com.humming.ascwg.requestUtils.OrderDetailRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.wg.order.dto.OrderDetailResponse;
import com.wg.order.dto.OrderHead;
import com.wg.order.dto.OrderItemDetail;
import com.wg.order.dto.ShippingAddressDetail;

import java.util.List;

/**
 * Created by Elvira on 2016/8/15.
 * 订单详情
 */
public class MyOrderDetailsActivity extends AbstractActivity {

    private TextView success;
    private LinearLayout addWineLayout;
    private View view;
    private View lineView;
    private TextView title;
    private ImageView back;
    public static final String ORDER_TYPE = "order_type";
    public static final String ORDER_ID = "order_id";
    public static final String KEY_TEXT = "key_text";
    public static final int ORDER_CODE = 1004;
    private TextView tvConsignee, tvPhone, tvAddress, tvOrderNumber, tvTime, tvTotal, tvDisCount, tvDisCountTotal, tvCancelOrder, tvPay;
    private OrderDetailResponse orderDetailResponse;
    private ShippingAddressDetail shippingAddress;
    private OrderHead orderHead;
    private List<OrderItemDetail> itemDetailInfo;
    private Context context;
    private String orderId = "";
    private ShoppingCartContent shoppingCartContent;
    private Application mAPP;
    private MainActivity.MyHandler mHandler;
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAPP = (Application) getApplication();
        // 获得共享变量实例
        mHandler = mAPP.getHandler();
        context = this;
        initView();//初始化VIew
        initData();//获取数据
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("订单详情");
        tvConsignee = (TextView) findViewById(R.id.content_my_order_details__consignee);
        tvPhone = (TextView) findViewById(R.id.content_my_order_details__phone);
        parent = (LinearLayout) findViewById(R.id.content_my_order_details__layouts);
        tvAddress = (TextView) findViewById(R.id.content_my_order_details__address);
        tvTime = (TextView) findViewById(R.id.content_my_order_details__creat_time);
        tvDisCount = (TextView) findViewById(R.id.content_my_order_details__discount);
        tvDisCountTotal = (TextView) findViewById(R.id.content_my_order_details__discount_price);
        tvOrderNumber = (TextView) findViewById(R.id.content_my_order_details__order_num);
        tvTotal = (TextView) findViewById(R.id.content_my_order_details__total_price);
        tvCancelOrder = (TextView) findViewById(R.id.content_my_order_details__cancel_order);
        tvPay = (TextView) findViewById(R.id.content_my_order_details__pay);
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.msgContent = "返回的数据";
                mHandler.sendEmptyMessage(0x123);
                finish();
            }
        });
        success = (TextView) findViewById(R.id.content_my_order_details__success);
        addWineLayout = (LinearLayout) findViewById(R.id.content_my_order_details__layout);
        tvCancelOrder.setVisibility(View.GONE);
        if ("1".equals(getIntent().getStringExtra(ORDER_TYPE))) {
            success.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.VISIBLE);
        } else {
            success.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
        }
        //去支付
        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                new PopupWindows(MyOrderDetailsActivity.this, parent);
            }
        });
        tvCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //取消订单
                CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
                cancelOrderRequest.setId(Long.parseLong(orderId));
                OkHttpClientManager.postAsyn(Config.CANCEL_ORDER, new OkHttpClientManager.ResultCallback<ResponseData>() {

                    @Override
                    public void onError(Request request, Error info) {
                        Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                        showShortToast(info.getInfo());
                    }

                    @Override
                    public void onResponse(ResponseData response) {
                        showShortToast(getResources().getString(R.string.order_status_5));
                        Bundle resultBundle = new Bundle();
                        resultBundle.putString(
                                MyOrderDetailsActivity.KEY_TEXT,
                                "");
                        Intent resultIntent = new Intent()
                                .putExtras(resultBundle);
                        setResult(
                                ORDER_CODE,
                                resultIntent);
                        finish();
                    }

                    @Override
                    public void onOtherError(Request request, Exception exception) {
                        Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                    }
                }, cancelOrderRequest, CancelOrderRequest.class);
            }
        });
    }


    private void initData() {
        orderId = getIntent().getStringExtra(ORDER_ID);
        if ("1".equals(getIntent().getStringExtra(ORDER_TYPE))) {
            if (Application.getInstance().getOrderDetailResponse() != null) {
                orderDetailResponse = Application.getInstance().getOrderDetailResponse();
                shippingAddress = orderDetailResponse.getShippingAddressInfo();
                orderHead = orderDetailResponse.getOrderHeadInfo();
                itemDetailInfo = orderDetailResponse.getItemDetailInfo();

                if (orderHead.getOrderStatus() == 1) {
                    success.setText(getBaseContext().getResources().getString(R.string.order_status_1));
                    tvCancelOrder.setVisibility(View.VISIBLE);
                } else if (orderHead.getOrderStatus() == 2) {
                    success.setText(getBaseContext().getResources().getString(R.string.order_status_2));
                } else if (orderHead.getOrderStatus() == 3) {
                    success.setText(getBaseContext().getResources().getString(R.string.order_status_3));
                } else if (orderHead.getOrderStatus() == 4) {
                    success.setText(getBaseContext().getResources().getString(R.string.order_status_4));
                } else if (orderHead.getOrderStatus() == 5) {
                    success.setText(getBaseContext().getResources().getString(R.string.order_status_5));
                } else if (orderHead.getOrderStatus() == 6) {
                    success.setText(getBaseContext().getResources().getString(R.string.order_status_6));
                }


                tvOrderNumber.setText(orderHead.getOrderNo());
                tvTime.setText(orderHead.getOrderTime());
                tvConsignee.setText(shippingAddress.getContact());
                tvPhone.setText(shippingAddress.getPhone());
                tvAddress.setText(Application.getInstance().getResources().getString(R.string.address_shop) + shippingAddress.getDetailAddress());
                tvDisCount.setText("×" + orderHead.getDiscount() + "%");
                tvDisCountTotal.setText(orderHead.getOrderSoldTotalPrice() + "");
                tvTotal.setText(orderHead.getOrderCostTotalPrice() + "");
                addWineList(itemDetailInfo.size());
            } else {
            }
        } else if (!"1".equals(getIntent().getStringExtra(ORDER_TYPE))) {
            OrderDetailRequest orderRequest = new OrderDetailRequest();
            orderRequest.setId(Long.parseLong(orderId));
            OkHttpClientManager.postAsyn(Config.ORDER_DETAIL, new OkHttpClientManager.ResultCallback<OrderDetailResponse>() {

                @Override
                public void onError(Request request, Error info) {
                    Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                    showShortToast(info.getInfo());
                }

                @Override
                public void onResponse(OrderDetailResponse response) {
                    orderDetailResponse = response;
                    shippingAddress = orderDetailResponse.getShippingAddressInfo();
                    orderHead = orderDetailResponse.getOrderHeadInfo();
                    itemDetailInfo = orderDetailResponse.getItemDetailInfo();

                    if (orderHead.getOrderStatus() == 1) {
                        success.setText(getBaseContext().getResources().getString(R.string.order_status_1));
                        tvCancelOrder.setVisibility(View.VISIBLE);
                    } else if (orderHead.getOrderStatus() == 2) {
                        success.setText(getBaseContext().getResources().getString(R.string.order_status_2));
                    } else if (orderHead.getOrderStatus() == 3) {
                        success.setText(getBaseContext().getResources().getString(R.string.order_status_3));
                    } else if (orderHead.getOrderStatus() == 4) {
                        success.setText(getBaseContext().getResources().getString(R.string.order_status_4));
                    } else if (orderHead.getOrderStatus() == 5) {
                        success.setText(getBaseContext().getResources().getString(R.string.order_status_5));
                    } else if (orderHead.getOrderStatus() == 6) {
                        success.setText(getBaseContext().getResources().getString(R.string.order_status_6));
                    }

                    tvOrderNumber.setText(orderHead.getOrderNo());
                    tvTime.setText(orderHead.getOrderTime());
                    tvConsignee.setText(shippingAddress.getContact());
                    tvPhone.setText(shippingAddress.getPhone());
                    tvAddress.setText(Application.getInstance().getResources().getString(R.string.address_shop) + shippingAddress.getDetailAddress());
                    tvDisCount.setText("×" + orderHead.getDiscount() + "%");
                    tvDisCountTotal.setText(orderHead.getOrderSoldTotalPrice() + "");
                    tvTotal.setText(orderHead.getOrderCostTotalPrice() + "");
                    addWineList(itemDetailInfo.size());
                }

                @Override
                public void onOtherError(Request request, Exception exception) {
                    Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                }
            }, orderRequest, OrderDetailResponse.class);

        }
    }

    //动态添加酒列表
    private void addWineList(int size) {
        for (int i = 0; i < size; i++) {
            view = LayoutInflater.from(MyOrderDetailsActivity.this).inflate(R.layout.item_my_order_, addWineLayout, false);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.item_my_order__layout_);
            ImageView wineImage = (ImageView) view.findViewById(R.id.item_my_order__wine_image);
            TextView wineName = (TextView) view.findViewById(R.id.item_my_order__wine_name);
            TextView winePrice = (TextView) view.findViewById(R.id.item_my_order__wine_price);
            TextView deletePrice = (TextView) view.findViewById(R.id.item_my_order__wine_delete_price);
            TextView wineNum = (TextView) view.findViewById(R.id.item_my_order__wine_num);
            deletePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            addWineLayout.addView(view);
            if (size > 1 && i < size - 1) {
                lineView = new View(MyOrderDetailsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lineView.setLayoutParams(lp);
                lineView.setBackgroundColor(ContextCompat.getColor(MyOrderDetailsActivity.this, R.color.garys));
                addWineLayout.addView(lineView);
            }
            OrderItemDetail orderItemDetail = itemDetailInfo.get(i);
            Picasso.with(context).load(orderItemDetail.getItemImage()).into(wineImage);
            wineName.setText(orderItemDetail.getItemNameCn() + "   " + orderItemDetail.getItemNameEn());
            winePrice.setText(orderItemDetail.getSoldUnitPrice() + "");
            deletePrice.setText(orderItemDetail.getCostUnitPrice() + "");
            wineNum.setText("×" + orderItemDetail.getQuantity() + "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.popupwindows_pay, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_in));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_out));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            setAnimationStyle(R.style.mypopwindow_anim_style);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            TextView aliPay = (TextView) view
                    .findViewById(R.id.item_popupwindows_alipay);
            TextView wxPay = (TextView) view
                    .findViewById(R.id.item_popupwindows_wxpay);
            TextView cancel = (TextView) view
                    .findViewById(R.id.item_popupwindows_cancel);
            aliPay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
            wxPay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }
}
