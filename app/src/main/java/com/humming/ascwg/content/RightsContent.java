package com.humming.ascwg.content;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.BusinessDetailsActivity;
import com.humming.ascwg.activity.rights.RightsDetailActivity;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.RightsGridAdapter;
import com.humming.ascwg.adapter.RightsListAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.wg.rights.dto.PointsRulesDetailResponse;
import com.wg.rights.dto.RightsDetailResponse;
import com.wg.rights.dto.RightsResponse;

import java.util.List;

/**
 * Created by Zhtq on 16/8/3.
 * <p/>
 * 我的权益
 */
public class RightsContent extends LinearLayout {

    private View view;

    private ImageView cardImage;
    private TextView cardNum;
    private TextView cardType;
    private TextView cardPoints;
    private TextView rulesHead;
    private RecyclerView listView;
    private RecyclerView gridView;
    private RightsListAdapter listAdapter;
    private RightsGridAdapter gridAdapter;
    private TextView rules;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    public RightsContent(Context context) {
        this(context, null);
    }

    public RightsContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_rights, this);
        initView();
        initData(context);
        rules = (TextView) findViewById(R.id.fragment_rights__rule);
    }

    //初始化数据
    public void initData(final Context c) {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.QUERY_RIGHTS, new OkHttpClientManager.ResultCallback<RightsResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(RightsResponse response) {
                final List<RightsDetailResponse> baseRights = response.getBaseRights();
                final List<RightsDetailResponse> exclusiveRights = response.getExclusiveRights();
                PointsRulesDetailResponse pointsRulesDetailResponse = response.getPointsRules();

                Picasso.with(c).load(response.getMemeberCardUrl()).into(cardImage);
                cardType.setText(response.getCardName());
                cardPoints.setText(response.getPoints() + "");
                cardNum.setText("NO." + response.getJdeSysId());
                linearLayoutManager = new LinearLayoutManager(getContext());
                listView.setLayoutManager(linearLayoutManager);
                listAdapter = new RightsListAdapter(baseRights);
                listView.setAdapter(listAdapter);
                listAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (1 == baseRights.get(position).getIsYumRight()) {//判断是否是百盛会员
                            Intent intent = new Intent(Application.getInstance().getApplicationContext(), RightsDetailActivity.class);
                            Application.getInstance().getCurrentActivity().startActivity(intent);
                        } else {

                        }
                    }
                });

                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                gridView.setLayoutManager(gridLayoutManager);
                gridAdapter = new RightsGridAdapter(exclusiveRights);
                gridView.setAdapter(gridAdapter);
                gridAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), BusinessDetailsActivity.class);
                        intent.putExtra(BusinessDetailsActivity.BUSINESS_IMAGE_DETAIL, exclusiveRights.get(position).getDescImage());
                        intent.putExtra(BusinessDetailsActivity.BUSINESS_PHONE, exclusiveRights.get(position).getBusinessPhone());
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                    }
                });

                String[] rule = pointsRulesDetailResponse.getRuleDetails();
                String ruleHead = pointsRulesDetailResponse.getRuleHead();
                rulesHead.setText(ruleHead);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < rule.length; i++) {
                    sb.append(rule[i]);
                    sb.append("\n");
                }
                rules.setText(sb.toString());
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, RightsResponse.class);
    }

    private void initView() {
        rules = (TextView) findViewById(R.id.fragment_rights__rule);
        rulesHead = (TextView) findViewById(R.id.fragment_rights__rule_head);
        cardImage = (ImageView) findViewById(R.id.fragment_rights__card_image);
        cardNum = (TextView) findViewById(R.id.fragment_rights__card_num);
        cardType = (TextView) findViewById(R.id.fragment_rights__card_type);
        cardPoints = (TextView) findViewById(R.id.fragment_rights__card_points);
        listView = (RecyclerView) findViewById(R.id.fragment_rights__listview);
        gridView = (RecyclerView) findViewById(R.id.fragment_rights__gridview);

    }


}
