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
import com.humming.ascwg.adapter.CityArrayAdapter;
import com.humming.ascwg.requestUtils.BaseInformation;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.baseinfo.dto.CityOrCountyResponse;

import java.util.List;

/**
 * Created by Zhtq on 2016/5/12.
 * 设置城市
 */
public class CitySelectActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView cityListView;
    private List<CityOrCountyResponse> cityResponses;
    private BaseInformation registerGetVerification;
    public static final String PROVINCE_CITY_CODE = "province_city_code";
    public static final String CITY_OR_COUNTY = "city_or_county";
    public static final int ACTIVITY_CITY_RESULT = 10022;
    public static final String KEY_TEXT = "city_text";
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private String type = "";//判断市还是区
    private String currentName = "";
    private TextView title;
    private ImageView back;
    private String id;
    private SwipeRefreshLayout refresh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoading.show();
        context = this;
        type = getIntent().getStringExtra(CITY_OR_COUNTY);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.city));
        registerGetVerification = new BaseInformation();
        cityListView = (RecyclerView) findViewById(R.id.content_select_area__listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_product_area__refresh);
        refresh.setOnRefreshListener(this);
        id = getIntent().getStringExtra(PROVINCE_CITY_CODE);
        registerGetVerification.setId(id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(type)) {
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(
                            CitySelectActivity.KEY_TEXT,
                            "");
                    Intent resultIntent = new Intent()
                            .putExtras(resultBundle);
                    setResult(
                            ACTIVITY_CITY_RESULT,
                            resultIntent);
                    finish();
                } else {
                    registerGetVerification = new BaseInformation();
                    registerGetVerification.setId(id);
                    OkHttpClientManager.postAsyn(Config.GET_CITY, new OkHttpClientManager.ResultCallback<List<CityOrCountyResponse>>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            showShortToast(info.getInfo());
                        }

                        @Override
                        public void onResponse(List<CityOrCountyResponse> response) {
                            type = "1";
                            cityResponses = response;
                            mLoading.hide();
                            linearLayoutManager = new LinearLayoutManager(context);
                            cityListView.setLayoutManager(linearLayoutManager);
                            CityArrayAdapter chapterListArrayAdapter = new CityArrayAdapter(Application.getInstance().getCurrentActivity(), cityResponses);
                            cityListView.setAdapter(chapterListArrayAdapter);
                            Animation animation = (Animation) AnimationUtils.loadAnimation(
                                    context, R.anim.fade_in2);
                            LayoutAnimationController lac = new LayoutAnimationController(animation);
                            lac.setDelay(0.25f);  //设置动画间隔时间
                            lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                            cityListView.setLayoutAnimation(lac);  //为ListView 添加动画
                            chapterListArrayAdapter.setmOnItemClickListener(new CityArrayAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    currentName = cityResponses.get(position).getName();
                                    registerGetVerification = new BaseInformation();
                                    String id = cityResponses.get(position).getId() + "";
                                    registerGetVerification.setId(id);
                                    OkHttpClientManager.postAsyn(Config.GET_CITY, new OkHttpClientManager.ResultCallback<List<CityOrCountyResponse>>() {
                                        @Override
                                        public void onError(Request request, Error info) {
                                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                                            showShortToast(info.getInfo());
                                        }

                                        @Override
                                        public void onResponse(List<CityOrCountyResponse> response) {
                                            type = "2";
                                            cityResponses = response;
                                            mLoading.hide();
                                            linearLayoutManager = new LinearLayoutManager(context);
                                            cityListView.setLayoutManager(linearLayoutManager);
                                            CityArrayAdapter chapterListArrayAdapter = new CityArrayAdapter(Application.getInstance().getCurrentActivity(), cityResponses);
                                            cityListView.setAdapter(chapterListArrayAdapter);
                                            chapterListArrayAdapter.setmOnItemClickListener(new CityArrayAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    currentName = currentName + cityResponses.get(position).getName();
                                                    //跳转至选择城市界面
                                                    Bundle resultBundle = new Bundle();
                                                    resultBundle.putString(
                                                            CitySelectActivity.KEY_TEXT,
                                                            currentName);
                                                    Intent resultIntent = new Intent()
                                                            .putExtras(resultBundle);
                                                    setResult(
                                                            ACTIVITY_CITY_RESULT,
                                                            resultIntent);
                                                    finish();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onOtherError(Request request, Exception exception) {
                                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                                            showShortToast(exception.getMessage());
                                        }
                                    }, registerGetVerification, new TypeReference<List<CityOrCountyResponse>>() {
                                    });
                                }
                            });
                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                            showShortToast(exception.getMessage());
                        }
                    }, registerGetVerification, new TypeReference<List<CityOrCountyResponse>>() {
                    });
                }

            }
        });
        OkHttpClientManager.postAsyn(Config.GET_CITY, new OkHttpClientManager.ResultCallback<List<CityOrCountyResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(List<CityOrCountyResponse> response) {
                cityResponses = response;
                mLoading.hide();
                linearLayoutManager = new LinearLayoutManager(context);
                cityListView.setLayoutManager(linearLayoutManager);
                CityArrayAdapter chapterListArrayAdapter = new CityArrayAdapter(Application.getInstance().getCurrentActivity(), cityResponses);
                cityListView.setAdapter(chapterListArrayAdapter);
                chapterListArrayAdapter.setmOnItemClickListener(new CityArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        currentName = cityResponses.get(position).getName();
                        registerGetVerification = new BaseInformation();
                        String id = cityResponses.get(position).getId() + "";
                        registerGetVerification.setId(id);
                        OkHttpClientManager.postAsyn(Config.GET_CITY, new OkHttpClientManager.ResultCallback<List<CityOrCountyResponse>>() {
                            @Override
                            public void onError(Request request, Error info) {
                                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                                showShortToast(info.getInfo());
                            }

                            @Override
                            public void onResponse(List<CityOrCountyResponse> response) {
                                type = "2";
                                cityResponses = response;
                                mLoading.hide();
                                linearLayoutManager = new LinearLayoutManager(context);
                                cityListView.setLayoutManager(linearLayoutManager);
                                CityArrayAdapter chapterListArrayAdapter = new CityArrayAdapter(Application.getInstance().getCurrentActivity(), cityResponses);
                                cityListView.setAdapter(chapterListArrayAdapter);
                                chapterListArrayAdapter.setmOnItemClickListener(new CityArrayAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        currentName = currentName + cityResponses.get(position).getName();
                                        //跳转至选择城市界面
                                        Bundle resultBundle = new Bundle();
                                        resultBundle.putString(
                                                CitySelectActivity.KEY_TEXT,
                                                currentName);
                                        Intent resultIntent = new Intent()
                                                .putExtras(resultBundle);
                                        setResult(
                                                ACTIVITY_CITY_RESULT,
                                                resultIntent);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onOtherError(Request request, Exception exception) {
                                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                            }
                        }, registerGetVerification, new TypeReference<List<CityOrCountyResponse>>() {
                        });
                    }
                });
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, registerGetVerification, new TypeReference<List<CityOrCountyResponse>>() {
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
