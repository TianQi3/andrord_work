package com.humming.ascwg.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.adapter.ItemCodeAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.item.dto.AllItemCodeResponse;

import java.util.ArrayList;
import java.util.List;

public class ItemCodeSelectorActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView listView;
    private Context context;
    private SearchView msearchView;
    private MenuItem seacherFilterMenu;
    ItemCodeAdapter itemCodeAdapter;
    RequestNull requestNull = new RequestNull();
    List<String> itemCodeList;
    List<String> itemCodeLists;
    List<String> itemCodeQueryList;
    private LinearLayoutManager linearLayoutManager;
    private TextView title,cancel;
    private ImageView back;
    private SwipeRefreshLayout refresh;
    private EditText serach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_code_selector);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        initView();//初始化数据
        getData();//获取数据
    }

    private void getData() {
        OkHttpClientManager.postAsyn(Config.PRODUCT_ALL_ITEM_CODE, new OkHttpClientManager.ResultCallback<AllItemCodeResponse>() {

            @Override
            public void onError(Request request, Error info) {
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(AllItemCodeResponse response) {
                mLoading.hide();
                itemCodeList = response.getItemCodeList();
                itemCodeLists = itemCodeList;
                linearLayoutManager = new LinearLayoutManager(context);
                listView.setLayoutManager(linearLayoutManager);
                itemCodeAdapter = new ItemCodeAdapter(context, itemCodeList);
                listView.setAdapter(itemCodeAdapter);
                Animation animation = (Animation) AnimationUtils.loadAnimation(
                        context, R.anim.fade_in2);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setDelay(0.25f);  //设置动画间隔时间
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                listView.setLayoutAnimation(lac);  //为ListView 添加动画
                setOnclickListener();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());

            }
        }, requestNull, AllItemCodeResponse.class);
    }

    private void initView() {
        mLoading.show();
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.item_code));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancel = (TextView) findViewById(R.id.content_product_itemcode_cancel);
        serach = (EditText) findViewById(R.id.content_product_itemcode__search);
        itemCodeQueryList = new ArrayList<String>();
        itemCodeLists = new ArrayList<String>();
        listView = (RecyclerView) findViewById(R.id.content_product_itemcode__listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_product_itemcode__refresh);
        refresh.setOnRefreshListener(this);
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
    }

    public void setOnclickListener() {
        itemCodeAdapter.setmOnItemClickListener(new ItemCodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                String item = itemCodeLists.get(position);
                bundle.putString(Constant.ITEM_CODE, item);
                Intent intent = new Intent().putExtras(bundle);
                intent.putExtras(bundle);
                setResult(Constant.ACTIVITY_RESULT_ITEM_CODE_SELECTED, intent);
                finish();
            }
        });
    }

    /*@Override
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
    }*/

    //关键字查询
    public void queryListCode(String keyWords) {
        itemCodeQueryList.clear();
        for (String value : itemCodeList) {
            if (value.toLowerCase().indexOf(keyWords.toLowerCase()) != -1) {
                itemCodeQueryList.add(value);
            }
        }
        itemCodeLists = itemCodeQueryList;
        linearLayoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(linearLayoutManager);
        itemCodeAdapter = new ItemCodeAdapter(context, itemCodeQueryList);
        listView.setAdapter(itemCodeAdapter);
        setOnclickListener();
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