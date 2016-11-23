package com.humming.ascwg.content;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.EventDetailsActivity;
import com.humming.ascwg.activity.PromotionDetailsActivity;
import com.humming.ascwg.activity.surprise.GiftActivity;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.EventAdapter;
import com.humming.ascwg.adapter.PromotionAdapter;
import com.humming.ascwg.requestUtils.PromotionDetail;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.event.dto.EventListResponse;
import com.wg.promotion.dto.PromotionDetailResponse;
import com.wg.promotion.dto.PromotionListResponse;

import java.util.List;
import java.util.Map;


/**
 * Created by Zhtq on 16/8/3.
 * <p/>
 * 惊喜
 */
public class SurpriseContent extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener {
    private View view;
    private SwipeRefreshLayout refresh;
    private RecyclerView listView;
    private EventAdapter eventAdapter;
    private PromotionAdapter promotionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Map<String, String>> list;
    private Context context;
    private boolean flag = true;
    private static final int TOTAL_COUNTER = 20;
    private static final int PAGE_SIZE = 10;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;

    private View notLoadingView;
    private List<EventListResponse> eventListResponses;
    private List<PromotionListResponse> promotionListResponses;


    public SurpriseContent(Context context) {
        this(context, null);
    }

    public SurpriseContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_surprise, this);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_surprise__refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        listView = (RecyclerView) view.findViewById(R.id.fragment_surprise__listview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);
        eventOrpromotion(flag);
    }

    //获取活动数据
    private void initEventDatas() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.EVENT_QUERY, new OkHttpClientManager.ResultCallback<List<EventListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<EventListResponse> response) {
                eventListResponses = response;
                initAdapter();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<EventListResponse>>() {
        });
    }


    //获取促销数据
    private void initPromotionDatas() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.PROMOTION_QUERY, new OkHttpClientManager.ResultCallback<List<PromotionListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<PromotionListResponse> response) {
                promotionListResponses = response;
                initAdapter();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<PromotionListResponse>>() {
        });
    }

    private void initAdapter() {
        if (flag) {
            eventAdapter = new EventAdapter(eventListResponses);
            eventAdapter.openLoadAnimation();
            listView.setAdapter(eventAdapter);
            Animation animation = (Animation) AnimationUtils.loadAnimation(
                    Application.getInstance().getCurrentActivity(), R.anim.fade_in2);
            LayoutAnimationController lac = new LayoutAnimationController(animation);
            lac.setDelay(0.25f);  //设置动画间隔时间
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
            listView.setLayoutAnimation(lac);  //为ListView 添加动画
            mCurrentCounter = eventAdapter.getData().size();
            eventAdapter.setOnLoadMoreListener(this);
            eventAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            eventAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), EventDetailsActivity.class);
                    intent.putExtra(EventDetailsActivity.EVENT_IMAGE_DETAIL, eventListResponses.get(position).getDescImage());
                    intent.putExtra(EventDetailsActivity.EVENT_ID, eventListResponses.get(position).getId() + "");
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                }
            });
        } else {
            promotionAdapter = new PromotionAdapter(promotionListResponses);
            promotionAdapter.openLoadAnimation();
            listView.setAdapter(promotionAdapter);
            Animation animation = (Animation) AnimationUtils.loadAnimation(
                    Application.getInstance().getCurrentActivity(), R.anim.fade_in2);
            LayoutAnimationController lac = new LayoutAnimationController(animation);
            lac.setDelay(0.25f);  //设置动画间隔时间
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
            listView.setLayoutAnimation(lac);  //为ListView 添加动画
            mCurrentCounter = promotionAdapter.getData().size();
            promotionAdapter.setOnLoadMoreListener(this);
            promotionAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            promotionAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                    final long type = promotionListResponses.get(position).getRuleType();
                    if (0L == type) {//显示图片
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), PromotionDetailsActivity.class);
                        intent.putExtra(PromotionDetailsActivity.PROMOTION_IMAGE_DETAIL, promotionListResponses.get(position).getDescImage());
                        intent.putExtra(PromotionDetailsActivity.PROMOTION_ID, promotionListResponses.get(position).getId() + "");
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                    } else {
                        PromotionDetail promotionDetail = new PromotionDetail();
                        promotionDetail.setPromotionId(promotionListResponses.get(position).getId());
                        OkHttpClientManager.postAsyn(Config.PROMOTION_DETAIL, new OkHttpClientManager.ResultCallback<PromotionDetailResponse>() {
                            @Override
                            public void onError(Request request, Error info) {
                                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(PromotionDetailResponse response) {
                                if (1L == type) {//和2差不多   A-A :gift

                                } else if (2L == type) { //A-B:gift
                                    Application.getInstance().setGifResponse(response.getGifResponse());
                                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), GiftActivity.class);
                                    intent.putExtra(GiftActivity.IMAGE_PATH, promotionListResponses.get(position).getBgImage());
                                    Application.getInstance().getCurrentActivity().startActivity(intent);
                                } else if (3L == type) {  //package
                                    Application.getInstance().setPackageRespones(response.getPackageRespones());
                                } else if (4L == type) { //select
                                    Application.getInstance().setSelectPackageRespones(response.getSelectPackageRespones());
                                } else if (5L == type) {//sales
                                    Application.getInstance().setSalesRespones(response.getSalesRespones());
                                }
                            }

                            @Override
                            public void onOtherError(Request request, Exception exception) {
                                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                            }
                        }, promotionDetail, PromotionDetailResponse.class);
                    }
                }
            });
        }
    }


    @Override
    public void onRefresh() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    eventAdapter.setNewData(eventListResponses);
                    eventAdapter.openLoadMore(PAGE_SIZE, true);
                    eventAdapter.removeAllFooterView();
                } else {
                    promotionAdapter.setNewData(promotionListResponses);
                    promotionAdapter.openLoadMore(PAGE_SIZE, true);
                    promotionAdapter.removeAllFooterView();
                }
                mCurrentCounter = PAGE_SIZE;
                refresh.setRefreshing(false);
            }
        }, delayMillis);
    }

    public void eventOrpromotion(boolean flag) {
        this.flag = flag;
        listView.removeAllViews();
        if (flag) {
            initEventDatas();
        } else {
            initPromotionDatas();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    if (flag) {
                        eventAdapter.notifyDataChangedAfterLoadMore(false);
                        eventAdapter.addFooterView(notLoadingView);
                    } else {
                        promotionAdapter.notifyDataChangedAfterLoadMore(false);
                        promotionAdapter.addFooterView(notLoadingView);
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                initEventDatas();
                                eventAdapter.notifyDataChangedAfterLoadMore(eventListResponses, true);
                                mCurrentCounter = eventAdapter.getData().size();
                            } else {
                                promotionAdapter.notifyDataChangedAfterLoadMore(promotionListResponses, true);
                                mCurrentCounter = promotionAdapter.getData().size();
                            }

                        }
                    }, delayMillis);
                }
            }

        });

    }

}
