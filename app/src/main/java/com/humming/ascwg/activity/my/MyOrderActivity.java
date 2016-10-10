package com.humming.ascwg.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.MyOrderAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.R;
import com.squareup.okhttp.Request;
import com.wg.order.dto.OrderListResponse;
import com.humming.ascwg.service.Error;

import java.util.List;

/**
 * Created by Elvira on 2016/8/8.
 * 历史订单
 */
public class MyOrderActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView listView;
    private List<OrderListResponse> orderListResponses;
    private LinearLayoutManager linearLayoutManager;
    private MyOrderAdapter myOrderAdapter;
    private TextView title;
    private ImageView back;
    private Context context;
    private View notLoadingView;

    private static final int TOTAL_COUNTER = 15;

    private static final int PAGE_SIZE = 5;

    private int delayMillis = 1000;

    private int mCurrentCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(Application.getInstance().getResources().getString(R.string.text_order));
        mLoading.show();
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_my_order__refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        listView = (RecyclerView) findViewById(R.id.content_my_order__listview);
        linearLayoutManager = new LinearLayoutManager(MyOrderActivity.this);
        listView.setLayoutManager(linearLayoutManager);
        initData();
    }

    private void initData() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.QUERY_ORDER, new OkHttpClientManager.ResultCallback<List<OrderListResponse>>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(List<OrderListResponse> response) {
                orderListResponses = response;
                initAdapter();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<OrderListResponse>>() {
        });
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    myOrderAdapter.notifyDataChangedAfterLoadMore(false);
                    if (notLoadingView == null) {
                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    }
                    myOrderAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myOrderAdapter.notifyDataChangedAfterLoadMore(orderListResponses, true);
                            mCurrentCounter = myOrderAdapter.getData().size();
                        }
                    }, delayMillis);
                }
            }

        });
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                myOrderAdapter.setNewData(orderListResponses);
                myOrderAdapter.openLoadMore(PAGE_SIZE, true);
                myOrderAdapter.removeAllFooterView();
                mCurrentCounter = PAGE_SIZE;
                refresh.setRefreshing(false);
            }
        }, delayMillis);
    }

    private void initAdapter() {
        mLoading.hide();
        myOrderAdapter = new MyOrderAdapter(orderListResponses);
        myOrderAdapter.openLoadAnimation();
        listView.setAdapter(myOrderAdapter);
        Animation animation = (Animation) AnimationUtils.loadAnimation(
                context, R.anim.fade_in2);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setDelay(0.25f);  //设置动画间隔时间
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
        listView.setLayoutAnimation(lac);  //为ListView 添加动画
        mCurrentCounter = myOrderAdapter.getData().size();
        myOrderAdapter.setOnLoadMoreListener(this);
        myOrderAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        myOrderAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyOrderDetailsActivity.class);
                intent.putExtra(MyOrderDetailsActivity.ORDER_TYPE, "0");
                intent.putExtra(MyOrderDetailsActivity.ORDER_ID, orderListResponses.get(position).getOrderHeadInfo().getOrderId() + "");
                startActivityForResult(intent, MyOrderDetailsActivity.ORDER_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        switch (requestCode) {
            case MyOrderDetailsActivity.ORDER_CODE:
                String text = resultBundle.getString(MyOrderDetailsActivity.KEY_TEXT);
                initData();
                break;
        }
    }
}
