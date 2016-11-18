package com.humming.ascwg.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.adapter.CountryAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.baseinfo.dto.CountryResponse;

import java.util.List;
import java.util.Locale;

public class CountryActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView listView;
    private CountryAdapter arrayAdapter;
    RequestNull requestNull = new RequestNull();
    private List<CountryResponse> countryResponseList;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private boolean asSelector = false;
    private TextView title;
    private ImageView back;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_country);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        initView();
        //拿数据
        OkHttpClientManager.postAsyn(Config.PRODUCT_ALL_COUNTRY, new OkHttpClientManager.ResultCallback<List<CountryResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(List<CountryResponse> response) {
                mLoading.hide();
                countryResponseList = response;
                linearLayoutManager = new LinearLayoutManager(context);
                listView.setLayoutManager(linearLayoutManager);
                arrayAdapter = new CountryAdapter(context, countryResponseList);
                listView.setAdapter(arrayAdapter);
                Animation animation = (Animation) AnimationUtils.loadAnimation(
                        context, R.anim.fade_in2);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setDelay(0.25f);  //设置动画间隔时间
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                listView.setLayoutAnimation(lac);  //为ListView 添加动画
                //点击事件
                arrayAdapter.setmOnItemClickListener(new CountryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        CountryResponse item = countryResponseList.get(position);
                        if (asSelector) {
                            Intent intent = new Intent().putExtras(bundle);
                            bundle.putString(Constant.EN, item.getNameEn());
                            bundle.putString(Constant.CN, item.getNameCn());
                            bundle.putString(Constant.ID, item.getId() + "");
                            intent.putExtras(bundle);
                            setResult(Constant.ACTIVITY_RESULT_COUNTRY_SELECTED, intent);
                            finish();
                        } else {
                            Intent intent = new Intent(context, WinesListActivity.class);
                            bundle.putString(Constant.ID, item.getId() + "");
                            String country = "";
                            Locale curLocal = getResources().getConfiguration().locale;
                            if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
                                country = item.getNameCn();
                            } else {
                                country = item.getNameEn();
                            }
                            bundle.putString(Constant.NAME, country);
                            bundle.putString(Constant.TYPE, "1");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<CountryResponse>>() {
        });
    }

    private void initView() {
        mLoading.show();
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.country));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            asSelector = bundle.getBoolean(Constant.AS_SELECTOR);
        }
        listView = (RecyclerView) findViewById(R.id.content_product_country__listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_product_country__refresh);
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);
    }
}
