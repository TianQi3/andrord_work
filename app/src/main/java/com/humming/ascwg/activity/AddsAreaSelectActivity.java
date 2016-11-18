package com.humming.ascwg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.my.AddressMessageActivity;
import com.humming.ascwg.adapter.AreaArraydAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.baseinfo.dto.ProvinceResponse;

import java.util.List;

/**
 * Created by Zhtq on 2016/5/12.
 * 设置地区
 */
public class AddsAreaSelectActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView areaListView;
    private List<ProvinceResponse> provinceResponses;
    public static final int ACTIVITY_AREA_RESULT = 10031;
    public static final String KEY_TEXT = "address_province";
    private String currentProvince;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private TextView title;
    private ImageView back;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        context = this;
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoading.show();
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.province));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle resultBundles = new Bundle();
                resultBundles.putString(
                        KEY_TEXT,
                        "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(ACTIVITY_AREA_RESULT,
                        resultIntent);
                finish();
            }
        });
        areaListView = (RecyclerView) findViewById(R.id.content_select_area__listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_product_area__refresh);
        refresh.setOnRefreshListener(this);
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_PROVINCE, new OkHttpClientManager.ResultCallback<List<ProvinceResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(List<ProvinceResponse> response) {
                provinceResponses = response;
                mLoading.hide();
                linearLayoutManager = new LinearLayoutManager(context);
                areaListView.setLayoutManager(linearLayoutManager);
                AreaArraydAdapter chapterListArrayAdapter = new AreaArraydAdapter(Application.getInstance().getCurrentActivity(), provinceResponses);
                areaListView.setAdapter(chapterListArrayAdapter);
                Animation animation = (Animation) AnimationUtils.loadAnimation(
                        context, R.anim.fade_in2);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setDelay(0.25f);  //设置动画间隔时间
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                areaListView.setLayoutAnimation(lac);  //为ListView 添加动画
                chapterListArrayAdapter.setmOnItemClickListener(new AreaArraydAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //跳转至选择城市界面
                        currentProvince = provinceResponses.get(position).getProvince();
                        AddressMessageActivity.provinceName = currentProvince;
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), AddsCitySelectActivity.class);
                        intent.putExtra(AddsCitySelectActivity.PROVINCE_CITY_CODE, provinceResponses.get(position).getProvinceId());
                        startActivityForResult(intent, AddsCitySelectActivity.ACTIVITY_CITY_RESULT);
                    }
                });
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                showShortToast(exception.getMessage());
            }
        }, requestNull, new TypeReference<List<ProvinceResponse>>() {
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        switch (requestCode) {
            case AddsCitySelectActivity.ACTIVITY_CITY_RESULT:
                String text = resultBundle.getString(AddsCitySelectActivity.KEY_TEXT);
                Bundle resultBundles = new Bundle();
                resultBundles.putString(
                        KEY_TEXT,
                        currentProvince + text);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(ACTIVITY_AREA_RESULT,
                        resultIntent);
                if ("".equals(text)) {

                } else {
                    finish();
                }

                break;
        }
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
