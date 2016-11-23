package com.humming.ascwg.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.adapter.WinesListAdapter;
import com.humming.ascwg.requestUtils.WinesQueryRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.item.dto.ItemListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 2016/8/12.
 * 酒的列表
 */
public class WinesListActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView listView;
    private WinesListAdapter arrayAdapter;
    private String id;
    private String type;
    private Context context;
    List<ItemListResponse> itemListResponseList;
    List<ItemListResponse> listResponses;
    private LinearLayoutManager linearLayoutManager;
    private TextView title, home, cancel;
    private LinearLayout noData;
    private SearchView msearchView;
    private MenuItem seacherFilterMenu;
    private SwipeRefreshLayout refresh;
    private ImageView back;
    private EditText serach;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wines_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回按钮
        //   getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        context = this;
        initView();//初始化数据
        getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        boolean asQueryResult = bundle.getBoolean(Constant.AS_QUERY_RESULT,
                false);
        if (asQueryResult) {
            String brand = bundle.getString(Constant.BRAND);
            String name = bundle.getString(Constant.NAME);
            String type = bundle.getString(Constant.TYPE);
            String itemCode = bundle.getString(Constant.ITEM_CODE);
            String country = bundle.getString(Constant.COUNTRY);
            WinesQueryRequest params = new WinesQueryRequest();
            params.setBrandId(brand);
            params.setCountryId(country);
            params.setItemCode(itemCode);
            params.setItemTypeId(type);
            params.setItemName(name);
            queryWinesList(params);
        } else {
            WinesQueryRequest params = new WinesQueryRequest();
            id = bundle.getString(Constant.ID);
            String name = bundle.getString(Constant.NAME);
            title.setText(name);
            type = bundle.getString(Constant.TYPE);
            if ("1".equals(type)) {//国家
                params.setCountryId(id);
            } else if ("2".equals(type)) {//type
                params.setItemTypeId(id);
            } else if ("3".equals(type)) {//brand
                params.setBrandId(id);
            } else {
            }
            queryWinesList(params);
        }
    }

    private void queryWinesList(WinesQueryRequest params) {
        OkHttpClientManager.postAsyn(Config.PRODUCT_WINES_QUERY, new OkHttpClientManager.ResultCallback<List<ItemListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(final List<ItemListResponse> response) {
                mLoading.hide();
                if (response.size() > 0) {
                    noData.setVisibility(View.GONE);
                    itemListResponseList = response;
                    linearLayoutManager = new LinearLayoutManager(context);
                    listView.setLayoutManager(linearLayoutManager);
                    arrayAdapter = new WinesListAdapter(context, itemListResponseList);
                    listView.setAdapter(arrayAdapter);
                    Animation animation = (Animation) AnimationUtils.loadAnimation(
                            context, R.anim.fade_in2);
                    LayoutAnimationController lac = new LayoutAnimationController(animation);
                    lac.setDelay(0.25f);  //设置动画间隔时间
                    lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                    listView.setLayoutAnimation(lac);  //为ListView 添加动画
                    arrayAdapter.setmOnItemClickListener(new WinesListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(context, WinesInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putLong(Constant.ID, response.get(position).getId());
                            bundle.putString(WinesInfoActivity.NORMAL_YUM_FLAG, "0");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, params, new TypeReference<List<ItemListResponse>>() {
        });
    }

    private void initView() {
        listResponses = new ArrayList<ItemListResponse>();
        mLoading.show();
        serach = (EditText) findViewById(R.id.content_product_wines_list__search);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        home = (TextView) findViewById(R.id.toolbar_home);
        cancel = (TextView) findViewById(R.id.content_product_wines_list_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.getInstance().finishAllActivity();
                Intent intents = new Intent(Application.getInstance().getApplicationContext(), MainActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intents);

            }
        });
        serach.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                serach.setFocusable(true);
                serach.setFocusableInTouchMode(true);
                serach.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(serach.getWindowToken(), 0);
                return false;
            }
        });
        serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryListCode(serach.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                queryListCode(serach.getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryListCode("");
                serach.clearFocus();
                cancel.setVisibility(View.GONE);
                serach.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                if (isOpen) {
                    // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
                    imm.hideSoftInputFromWindow(serach.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        serach.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    cancel.setVisibility(View.VISIBLE);
                } else {
                    // 此处为失去焦点时的处理内容
                    cancel.setVisibility(View.GONE);
                }
            }
        });
        listView = (RecyclerView) findViewById(R.id.content_product_wineslist__listview);
        noData = (LinearLayout) findViewById(R.id.content_wine_list__no_data);
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_product_wineslist__refresh);
        refresh.setOnRefreshListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        seacherFilterMenu = menu.findItem(R.id.action_search);
//        msearchView = (SearchView) MenuItemCompat.getActionView(seacherFilterMenu);
//        MenuItemCompat.setOnActionExpandListener(seacherFilterMenu, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                //点击返回按钮
//                queryListCode("");
//                return true;
//            }
//        });
//        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                queryListCode(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                queryListCode(newText);
//                return false;
//            }
//        });
//        return true;
//    }

    //关键字查询
    public void queryListCode(String keyWords) {
        listResponses.clear();
        for (ItemListResponse value : itemListResponseList) {
            if (value.getNameCn().toLowerCase().contains(keyWords)
                    || value.getNameEn().contains(keyWords)) {
                listResponses.add(value);
            }
        }
        linearLayoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(linearLayoutManager);
        arrayAdapter = new WinesListAdapter(context, listResponses);
        listView.setAdapter(arrayAdapter);
        Animation animation = (Animation) AnimationUtils.loadAnimation(
                context, R.anim.fade_in2);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setDelay(0.25f);  //设置动画间隔时间
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
        listView.setLayoutAnimation(lac);  //为ListView 添加动画
        arrayAdapter.setmOnItemClickListener(new WinesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, WinesInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.ID, listResponses.get(position).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

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
