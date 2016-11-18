package com.humming.ascwg.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.adapter.BrandAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.baseinfo.dto.BrandResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BrandActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView listView;
    private SearchView msearchView;
    private MenuItem seacherFilterMenu;
    private BrandAdapter arrayAdapter;
    RequestNull requestNull = new RequestNull();
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private List<BrandResponse> brandList;
    private List<BrandResponse> brandQueryList;
    private boolean asSelector = false;
    private TextView title;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brands);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        initView();//初始化数据
        getData();//获取数据
    }

    private void getData() {
        OkHttpClientManager.postAsyn(Config.PRODUCT_ALL_BRAND, new OkHttpClientManager.ResultCallback<List<BrandResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(List<BrandResponse> response) {
                mLoading.hide();
                brandList = response;
                linearLayoutManager = new LinearLayoutManager(context);
                listView.setLayoutManager(linearLayoutManager);
                arrayAdapter = new BrandAdapter(context, brandList);
                listView.setAdapter(arrayAdapter);
                Animation animation = (Animation) AnimationUtils.loadAnimation(
                        context, R.anim.fade_in2);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setDelay(0.25f);  //设置动画间隔时间
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                listView.setLayoutAnimation(lac);  //为ListView 添加动画
                arrayAdapter.setmOnItemClickListener(new BrandAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        BrandResponse item = brandList.get(position);
                        if (asSelector) {
                            Intent intent = new Intent().putExtras(bundle);
                            bundle.putString(Constant.EN, item.getNameEn());
                            bundle.putString(Constant.CN, item.getNameCn());
                            bundle.putString(Constant.ID, item.getId() + "");
                            intent.putExtras(bundle);
                            setResult(Constant.ACTIVITY_RESULT_BRAND_SELECTED, intent);
                            finish();
                        } else {
                            Intent intent = new Intent(context, WinesListActivity.class);
                            bundle.putString(Constant.ID, item.getId() + "");
                            Locale curLocal = getResources().getConfiguration().locale;
                            String brandName = "";
                            if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
                                brandName = item.getNameCn();
                            } else {
                                brandName = item.getNameEn();
                            }
                            bundle.putString(Constant.NAME, brandName);
                            bundle.putString(Constant.TYPE, "3");
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
        }, requestNull, new TypeReference<List<BrandResponse>>() {
        });
    }

    private void initView() {
        mLoading.show();
        brandQueryList = new ArrayList<BrandResponse>();
        title = (TextView) findViewById(R.id.toolbar_title);
        // back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.brand));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            asSelector = bundle.getBoolean(Constant.AS_SELECTOR);
        }
        listView = (RecyclerView) findViewById(R.id.content_product_brands__listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_product_brands__refresh);
        refresh.setOnRefreshListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        seacherFilterMenu = menu.findItem(R.id.action_search);
        msearchView = (SearchView) MenuItemCompat.getActionView(seacherFilterMenu);
        MenuItemCompat.setOnActionExpandListener(seacherFilterMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //点击返回按钮
                queryListCode("");
                return true;
            }
        });
        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryListCode(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryListCode(newText);
                return false;
            }
        });
        return true;
    }

    //关键字查询
    public void queryListCode(String keyWords) {
        brandQueryList.clear();
        for (BrandResponse value : brandList) {
            if (value.getNameCn().toLowerCase().contains(keyWords)
                    || value.getNameEn().contains(keyWords)) {
                brandQueryList.add(value);
            }
        }
        linearLayoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(linearLayoutManager);
        arrayAdapter = new BrandAdapter(context, brandQueryList);
        listView.setAdapter(arrayAdapter);
        Animation animation = (Animation) AnimationUtils.loadAnimation(
                context, R.anim.fade_in2);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setDelay(0.25f);  //设置动画间隔时间
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
        listView.setLayoutAnimation(lac);  //为ListView 添加动画
        arrayAdapter.setmOnItemClickListener(new BrandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                BrandResponse item = brandQueryList.get(position);
                if (asSelector) {
                    Intent intent = new Intent().putExtras(bundle);
                    bundle.putString(Constant.EN, item.getNameEn());
                    bundle.putString(Constant.CN, item.getNameCn());
                    bundle.putString(Constant.ID, item.getId() + "");
                    intent.putExtras(bundle);
                    setResult(Constant.ACTIVITY_RESULT_BRAND_SELECTED, intent);
                    finish();
                } else {
                    Intent intent = new Intent(context, WinesListActivity.class);
                    bundle.putString(Constant.ID, item.getId() + "");
                    String brandName = item.getNameEn();
                    if (Locale.getDefault().getCountry().equalsIgnoreCase("CN")) {
                        brandName = item.getNameCn();
                    }
                    bundle.putString(Constant.NAME, brandName);
                    bundle.putString(Constant.TYPE, "3");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
