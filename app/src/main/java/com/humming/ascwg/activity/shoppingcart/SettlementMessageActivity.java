package com.humming.ascwg.activity.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.my.MyAddressActivity;
import com.humming.ascwg.activity.my.MyOrderDetailsActivity;
import com.humming.ascwg.adapter.SettlementAdapter;
import com.humming.ascwg.model.OrderSelect;
import com.humming.ascwg.requestUtils.OrderItemInfo;
import com.humming.ascwg.requestUtils.OrderRequest;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Request;
import com.wg.order.dto.OrderDetailResponse;
import com.wg.order.dto.ShippingAddressListResponse;

import java.util.ArrayList;
import java.util.List;

public class SettlementMessageActivity extends AbstractActivity {

    private LinearLayout selectAddress, defaultAddress;
    private RecyclerView orderListView;
    private TextView total, tvContact, tvPhone, tvAddress, tvDiscountTotal, tvDiscount;
    private Button commit;
    private List<OrderSelect> orderSelectList;
    private List<OrderSelect> rightSelectList;
    private List<OrderSelect> allLists;
    private Context context;
    public static String TOTAL = "total";
    private TextView title;
    private ImageView back;
    private List<OrderItemInfo> orderItemLists;
    private long shippingAddressId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_message);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        orderItemLists = new ArrayList<OrderItemInfo>();
        initView();//初始化数据
        initData();
    }

    private void initData() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ADDRESS_DEFALULT, new OkHttpClientManager.ResultCallback<ShippingAddressListResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ShippingAddressListResponse response) {
                if (response != null && response.getShippingAddressId() > 0L) {
                    selectAddress.setVisibility(View.GONE);
                    defaultAddress.setVisibility(View.VISIBLE);
                    tvPhone.setText(response.getPhone() + "");
                    tvContact.setText(response.getContact());
                    shippingAddressId = response.getShippingAddressId();
                    tvAddress.setText(response.getDetailAddress());
                } else {
                    selectAddress.setVisibility(View.VISIBLE);
                    defaultAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, ShippingAddressListResponse.class);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.settlement_message));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderListView = (RecyclerView) findViewById(R.id.content_settlement__order_listview);
        selectAddress = (LinearLayout) findViewById(R.id.content_settlement__select_address);
        defaultAddress = (LinearLayout) findViewById(R.id.content_settlement__default_address);
        tvAddress = (TextView) findViewById(R.id.content_settlement_message__address);
        tvContact = (TextView) findViewById(R.id.content_settlement_message__consignee);
        tvPhone = (TextView) findViewById(R.id.content_settlement_message__phone);
        commit = (Button) findViewById(R.id.content_settlement_message__commit);
        total = (TextView) findViewById(R.id.content_settlement_message__total);
        tvDiscountTotal = (TextView) findViewById(R.id.content_settlement_message__discount_total);
        tvDiscount = (TextView) findViewById(R.id.content_settlement_message__discount);
        String totals = getIntent().getStringExtra(TOTAL);
        total.setText(totals);
        String discount = SharePrefUtil.getString(Constant.FILE_NAME, Constant.DISCOUNT, "", this);
        Double s = Double.parseDouble(totals) * Integer.parseInt(discount) / 100;
        // Float totalss = Float.parseFloat(String.valueOf(Integer.parseInt(totals) * Integer.parseInt(Application.getInstance().getDiscountPrice()) / 100));
        tvDiscountTotal.setText(s + "");
        tvDiscount.setText(Application.getInstance().getResources().getString(R.string.vip_discount) + " " + discount + "%");
        allLists = new ArrayList<OrderSelect>();
        if (Application.getInstance().getOrderSelectList() != null) {
            orderSelectList = Application.getInstance().getOrderSelectList();
            for (OrderSelect o : orderSelectList) {
                if (o.isSelect()) {
                    OrderItemInfo info = new OrderItemInfo();
                    info.setQuantity(o.getQuantity());
                    info.setShoppingCartId(o.getShoppingCartId());
                    orderItemLists.add(info);
                    allLists.add(o);
                }
            }

        } else {
        }
        if (Application.getInstance().getRightSelectList() != null) {
            rightSelectList = Application.getInstance().getRightSelectList();
            for (OrderSelect o : rightSelectList) {
                if (o.isSelect()) {
                    OrderItemInfo info = new OrderItemInfo();
                    info.setQuantity(o.getQuantity());
                    info.setShoppingCartId(o.getShoppingCartId());
                    orderItemLists.add(info);
                    allLists.add(o);
                }
            }
        } else {
        }
        SettlementAdapter settlementAdapter = new SettlementAdapter(context, allLists);
        orderListView.setAdapter(settlementAdapter);
        orderListView.setLayoutManager(new LinearLayoutManager(this));
        //选择地址
        selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyAddressActivity.class);
                startActivityForResult(intent, MyAddressActivity.ADDRESS_CODE);
            }
        });
        defaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyAddressActivity.class);
                startActivityForResult(intent, MyAddressActivity.ADDRESS_CODE);
            }
        });
        //提交订单
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderRequest orderQuest = new OrderRequest();
                orderQuest.setItems(orderItemLists);
                orderQuest.setShippingAddressId(shippingAddressId);
                OkHttpClientManager.postAsyn(Config.INSERT_ORDER, new OkHttpClientManager.ResultCallback<OrderDetailResponse>() {
                    @Override
                    public void onError(Request request, Error info) {
                        Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                        showShortToast(info.getInfo());
                    }

                    @Override
                    public void onResponse(OrderDetailResponse response) {
                        showShortToast(Application.getInstance().getResources().getString(R.string.commit_order_success));
                        Application.getInstance().setOrderDetailResponse(response);
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyOrderDetailsActivity.class);
                        intent.putExtra(MyOrderDetailsActivity.ORDER_ID, response.getOrderHeadInfo().getOrderId() + "");
                        intent.putExtra(MyOrderDetailsActivity.ORDER_TYPE, "1");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onOtherError(Request request, Exception exception) {
                        Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                    }
                }, orderQuest, OrderDetailResponse.class);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        switch (requestCode) {
            case MyAddressActivity.ADDRESS_CODE:
                String text = resultBundle.getString(MyAddressActivity.GET_SHIPPINGADDRESSID);
                if ("".equals(text)) {//点击返回

                } else {
                    shippingAddressId = Long.parseLong(text);
                    tvContact.setText(resultBundle.getString(MyAddressActivity.GET_NAME));
                    tvPhone.setText(resultBundle.getString(MyAddressActivity.GET_PHONE));
                    tvAddress.setText(resultBundle.getString(MyAddressActivity.GET_ADDRESS));
                }
                break;
        }
    }
}
