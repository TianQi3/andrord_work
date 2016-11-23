package com.humming.ascwg.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.adapter.MyAddressAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.order.dto.ShippingAddressListResponse;
import com.humming.ascwg.service.Error;

import java.util.List;

/**
 * Created by Elvira on 2016/8/8.
 * 地址管理
 */
public class MyAddressActivity extends AbstractActivity {
    private TextView addAddress;
    private LinearLayout noAddress;
    private ListView addressListView;
    private Context context;
    private TextView title;
    private ImageView back;
    private static int[] itemPageResArray;
    private MyAddressAdapter mAdapter;
    private List<ShippingAddressListResponse> addressLists;
    public static final int ADDRESS_CODE = 1003;
    public static final String ADDRESS_TYPE = "address_type";
    public static final String GET_ADDRESS = "get_address";
    public static final String GET_NAME = "get_name";
    public static final String GET_PHONE = "get_phone";
    public static final String GET_SHIPPINGADDRESSID = "get_shippingadressid";
    public static String addressType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        mLoading.show();
        initView();//初始化数据
        initData();//收货地址查询
    }

    private void initData() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ADDRESS_QUERY, new OkHttpClientManager.ResultCallback<List<ShippingAddressListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(List<ShippingAddressListResponse> response) {
                addressLists = response;
                mLoading.hide();
                if (addressLists.size() > 0) {
                    noAddress.setVisibility(View.GONE);
                    addressListView.setVisibility(View.VISIBLE);
                    mAdapter = new MyAddressAdapter(Application.getInstance().getCurrentActivity(),
                            R.layout.list_item_address_delete, addressLists, itemPageResArray, 0, "");
                    addressListView.setAdapter(mAdapter);
                    Animation animation = (Animation) AnimationUtils.loadAnimation(
                            context, R.anim.fade_in2);
                    LayoutAnimationController lac = new LayoutAnimationController(animation);
                    lac.setDelay(0.25f);  //设置动画间隔时间
                    lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                    addressListView.setLayoutAnimation(lac);  //为ListView 添加动画

                } else {
                    addressListView.setVisibility(View.GONE);
                    noAddress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<ShippingAddressListResponse>>() {
        });
    }

    private void initView() {
        addressType = getIntent().getStringExtra(ADDRESS_TYPE);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        itemPageResArray = new int[]{R.layout.list_item_address, R.layout.list_item_view_page_right};
        if (addressType != null && "1".equals(addressType)) {//从我的里面进入
            title.setText(getResources().getString(R.string.my_address));
        } else {
            title.setText(getResources().getString(R.string.select_address_shop));
        }

        addAddress = (TextView) findViewById(R.id.content_my_adress__add);
        noAddress = (LinearLayout) findViewById(R.id.content_my_address__no_address);
        addressListView = (ListView) findViewById(R.id.content_my_adress__listview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle resultBundles = new Bundle();
                resultBundles.putString(
                        GET_SHIPPINGADDRESSID,
                        "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(ADDRESS_CODE,
                        resultIntent);
                finish();
            }
        });
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), AddressMessageActivity.class);
                intent.putExtra(AddressMessageActivity.UPDATE_OR_ADD, "1");
                startActivityForResult(intent, AddressMessageActivity.ACTIVITY_ADDRESS_RESULT);
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
            case AddressMessageActivity.ACTIVITY_ADDRESS_RESULT:
                String text = resultBundle.getString(AddressMessageActivity.KEY_TEXT);
                if ("1".equals(text)) {//添加地址返回需要刷新
                    initData();
                } else {//直接返回

                }
                break;
        }
    }
}
