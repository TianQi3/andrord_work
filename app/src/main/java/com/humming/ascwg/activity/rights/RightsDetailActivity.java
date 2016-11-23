package com.humming.ascwg.activity.rights;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.product.WinesInfoActivity;
import com.humming.ascwg.adapter.BaseAdapter;
import com.humming.ascwg.adapter.RightsDetailAdapter;
import com.humming.ascwg.requestUtils.RightsRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.item.dto.YumItemListResponse;

import java.util.List;


/**
 * Created by Zhtq on 16/11/7.
 */

public class RightsDetailActivity extends AbstractActivity {
    private RecyclerView mRecycleView;
    private RightsDetailAdapter mAdapter;
    private List<YumItemListResponse> listResponses;
    private ImageView back;
    private TextView title;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_detail);
        initView();
        initData();

    }

    private void initView() {
        mLoading.show();
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(Application.getInstance().getResources().getString(R.string.yum_member_zone));
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecycleView = (RecyclerView) findViewById(R.id.activity_right_detail__recycleview);
    }

    private void initData() {
        RightsRequest baseInformation = new RightsRequest();
        baseInformation.setPage(1);
        OkHttpClientManager.postAsyn(Config.GET_YUM_LIST, new OkHttpClientManager.ResultCallback<List<YumItemListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<YumItemListResponse> response) {
                mLoading.hide();
                listResponses = response;
                linearLayoutManager = new LinearLayoutManager(getBaseContext());
                mRecycleView.setLayoutManager(linearLayoutManager);
                mAdapter = new RightsDetailAdapter(listResponses);
                mRecycleView.setAdapter(mAdapter);
                Animation animation = (Animation) AnimationUtils.loadAnimation(
                        getBaseContext(), R.anim.fade_in2);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setDelay(0.25f);  //设置动画间隔时间
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
                mRecycleView.setLayoutAnimation(lac);  //为ListView 添加动画
                mAdapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), WinesInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.ID, listResponses.get(position).getItemId());
                        bundle.putString(WinesInfoActivity.NORMAL_YUM_FLAG,"1");
                        intent.putExtras(bundle);
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                    }
                });
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, baseInformation, new TypeReference<List<YumItemListResponse>>() {
        });
    }
}
