package com.humming.ascwg.activity.my;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.EventDetailsActivity;
import com.humming.ascwg.activity.PromotionDetailsActivity;
import com.humming.ascwg.activity.surprise.GiftActivity;
import com.humming.ascwg.adapter.AddBillAdapter;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.EventAdapter;
import com.humming.ascwg.adapter.NormalBillAdapter;
import com.humming.ascwg.adapter.PromotionAdapter;
import com.humming.ascwg.requestUtils.PromotionDetail;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.baseinfo.dto.NorInvoiceListResponse;
import com.wg.baseinfo.dto.SepInvoiceListResponse;
import com.wg.event.dto.EventListResponse;
import com.wg.promotion.dto.PromotionDetailResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/8.
 * 发票管理
 */
public class MyBillActivity extends AbstractActivity {
    private TextView title, normalBill, addBill;
    private ImageView back;
    private RecyclerView listView;
    private NormalBillAdapter normalBillAdapter;
    private AddBillAdapter addBillAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean flag = true;

    private List<NorInvoiceListResponse> norLists;
    private List<SepInvoiceListResponse> sepLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.toolbar_back);
        listView = (RecyclerView) findViewById(R.id.content_my_bill__listview);
        linearLayoutManager = new LinearLayoutManager(getBaseContext());
        listView.setLayoutManager(linearLayoutManager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        normalBill = (TextView) findViewById(R.id.toolbar_normal_bill);
        addBill = (TextView) findViewById(R.id.toolbar_add_bill);
        normarlOradd(flag);
        normalBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalBill.setTextColor(getResources().getColor(R.color.money_vip));
                addBill.setTextColor(getResources().getColor(R.color.tab_text));
                normarlOradd(true);
            }
        });
        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalBill.setTextColor(getResources().getColor(R.color.tab_text));
                addBill.setTextColor(getResources().getColor(R.color.money_vip));
                normarlOradd(false);
            }
        });
    }

    public void normarlOradd(boolean flag) {
        this.flag = flag;
        listView.removeAllViews();
        if (flag) {
            initNormalDatas();
        } else {
            initAddDatas();
        }
    }

    //获取普票数据
    private void initNormalDatas() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.QUERY_NOR, new OkHttpClientManager.ResultCallback<List<NorInvoiceListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<NorInvoiceListResponse> response) {
                norLists = response;
                initAdapter();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<NorInvoiceListResponse>>() {
        });
    }

    //获取普票数据
    private void initAddDatas() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.QUERY_SEP, new OkHttpClientManager.ResultCallback<List<SepInvoiceListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<SepInvoiceListResponse> response) {
                sepLists = response;
                initAdapter();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<SepInvoiceListResponse>>() {
        });
    }

    private void initAdapter() {
        if (flag) {
            normalBillAdapter = new NormalBillAdapter(norLists);
            listView.setAdapter(normalBillAdapter);
            Animation animation = (Animation) AnimationUtils.loadAnimation(
                    Application.getInstance().getCurrentActivity(), R.anim.fade_in2);
            LayoutAnimationController lac = new LayoutAnimationController(animation);
            lac.setDelay(0.25f);  //设置动画间隔时间
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
            listView.setLayoutAnimation(lac);  //为ListView 添加动画
            normalBillAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    /*Intent intent = new Intent(Application.getInstance().getCurrentActivity(), EventDetailsActivity.class);
                    intent.putExtra(EventDetailsActivity.EVENT_IMAGE_DETAIL, eventListResponses.get(position).getDescImage());
                    intent.putExtra(EventDetailsActivity.EVENT_ID, eventListResponses.get(position).getId() + "");
                    Application.getInstance().getCurrentActivity().startActivity(intent);*/
                }
            });
        } else {
            addBillAdapter = new AddBillAdapter(sepLists);
            addBillAdapter.openLoadAnimation();
            listView.setAdapter(addBillAdapter);
            Animation animation = (Animation) AnimationUtils.loadAnimation(
                    Application.getInstance().getCurrentActivity(), R.anim.fade_in2);
            LayoutAnimationController lac = new LayoutAnimationController(animation);
            lac.setDelay(0.25f);  //设置动画间隔时间
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
            listView.setLayoutAnimation(lac);  //为ListView 添加动画
        }
    }
}
