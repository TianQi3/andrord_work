package com.humming.ascwg.activity.my;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.EventDetailsActivity;
import com.humming.ascwg.activity.PromotionDetailsActivity;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.EventAdapter;
import com.humming.ascwg.adapter.PromotionAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.event.dto.EventListResponse;
import com.wg.promotion.dto.PromotionListResponse;

import java.util.List;

;

/**
 * Created by Elvira on 2016/8/8.
 * 活动
 */
public class MyEventActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener {
    private TextView titleEvent, titlePromotion, nullText;
    private ImageView back;
    private SwipeRefreshLayout refresh;
    private RecyclerView listView;
    private EventAdapter eventAdapter;
    private PromotionAdapter promotionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout surpriceNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlePromotion = (TextView) findViewById(R.id.toolbar_my_promotion);
        titleEvent = (TextView) findViewById(R.id.toolbar_my_activity);
        refresh = (SwipeRefreshLayout) findViewById(R.id.fragment_surprise__refresh);
        refresh.setColorSchemeResources(R.color.colorAccent);
        refresh.setOnRefreshListener(this);
        listView = (RecyclerView) findViewById(R.id.fragment_surprise__listview);
        surpriceNull = (LinearLayout) findViewById(R.id.fragment_surprice__null);
        nullText = (TextView) findViewById(R.id.fragment_surprice__null_text);
        linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);
        titleEvent.setOnClickListener(new View.OnClickListener() {//我的活动
            @Override
            public void onClick(View v) {
                mLoading.show();
                titleEvent.setTextColor(getResources().getColor(R.color.tab_bg));
                titlePromotion.setTextColor(getResources().getColor(R.color.tab_text));
                initMyData(true);
            }
        });
        titlePromotion.setOnClickListener(new View.OnClickListener() {//我的促销
            @Override
            public void onClick(View v) {
                mLoading.show();
                titlePromotion.setTextColor(getResources().getColor(R.color.tab_bg));
                titleEvent.setTextColor(getResources().getColor(R.color.tab_text));
                initMyData(false);
            }
        });
        mLoading.show();
        initMyData(true);
    }

    private void initMyData(boolean eventOrPromotion) {
        RequestNull requestNull = new RequestNull();
        if (eventOrPromotion) {
            OkHttpClientManager.postAsyn(Config.MY_EVENT, new OkHttpClientManager.ResultCallback<List<EventListResponse>>() {
                @Override
                public void onError(Request request, Error info) {
                    Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                    showShortToast(info.getInfo());
                }

                @Override
                public void onResponse(final List<EventListResponse> response) {
                    if (response.size() > 0) {
                        surpriceNull.setVisibility(View.GONE);
                        eventAdapter = new EventAdapter(response);
                        eventAdapter.openLoadAnimation();
                        mLoading.hide();
                        listView.setAdapter(eventAdapter);
                        Animation animation = (Animation) AnimationUtils.loadAnimation(
                                Application.getInstance().getCurrentActivity(), R.anim.fade_in2);
                        LayoutAnimationController lac = new LayoutAnimationController(animation);
                        lac.setDelay(0.25f);  //设置动画间隔时间
                        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                        listView.setLayoutAnimation(lac);  //为ListView 添加动画
                        eventAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), EventDetailsActivity.class);
                                intent.putExtra(EventDetailsActivity.EVENT_IMAGE_DETAIL, response.get(position).getDescImage());
                                intent.putExtra(EventDetailsActivity.EVENT_ID, response.get(position).getId() + "");
                                intent.putExtra(EventDetailsActivity.EVENT_MY, "1");
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                            }
                        });
                    } else {
                        mLoading.hide();
                        surpriceNull.setVisibility(View.VISIBLE);
                        nullText.setText(getResources().getString(R.string.activity_null));
                    }
                }

                @Override
                public void onOtherError(Request request, Exception exception) {
                    Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                }
            }, requestNull, new TypeReference<List<EventListResponse>>() {
            });
        } else {
            OkHttpClientManager.postAsyn(Config.MY_PROMOTION, new OkHttpClientManager.ResultCallback<List<PromotionListResponse>>() {
                @Override
                public void onError(Request request, Error info) {
                    Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                    showShortToast(info.getInfo());
                }

                @Override
                public void onResponse(final List<PromotionListResponse> response) {
                    if (response.size() > 0) {
                        surpriceNull.setVisibility(View.GONE);
                        promotionAdapter = new PromotionAdapter(response);
                        promotionAdapter.openLoadAnimation();
                        mLoading.hide();
                        listView.setAdapter(promotionAdapter);
                        Animation animation = (Animation) AnimationUtils.loadAnimation(
                                Application.getInstance().getCurrentActivity(), R.anim.fade_in2);
                        LayoutAnimationController lac = new LayoutAnimationController(animation);
                        lac.setDelay(0.25f);  //设置动画间隔时间
                        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                        listView.setLayoutAnimation(lac);  //为ListView 添加动画
                        promotionAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), PromotionDetailsActivity.class);
                                intent.putExtra(PromotionDetailsActivity.PROMOTION_IMAGE_DETAIL, response.get(position).getDescImage());
                                intent.putExtra(PromotionDetailsActivity.PROMOTION_ID, response.get(position).getId() + "");
                                intent.putExtra(PromotionDetailsActivity.PROMOTION_MY, "1");
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                            }
                        });
                    } else {
                        mLoading.hide();
                        surpriceNull.setVisibility(View.VISIBLE);
                        nullText.setText(getResources().getString(R.string.promotion_null));
                    }
                }

                @Override
                public void onOtherError(Request request, Exception exception) {
                    Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                }
            }, requestNull, new TypeReference<List<PromotionListResponse>>() {
            });
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
