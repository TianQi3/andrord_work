package com.humming.ascwg.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.ascwg.Application;
import com.humming.ascwg.activity.shoppingcart.SettlementMessageActivity;
import com.humming.ascwg.adapter.CartRecycleAdapter;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.squareup.okhttp.Request;
import com.wg.order.dto.ShoppingCartListResponse;

import java.util.List;


/**
 * Created by Zhtq on 16/8/3.
 * <p>
 * 购物车
 */
public class ShoppingCartContent extends LinearLayout {
    private View view;
    private static ListView mRecycleView;
    private static CartRecycleAdapter mAdapter;
    private static List<ShoppingCartListResponse> listResponses;
    private static int[] itemPageResArray;
    private static CheckBox selectAll;
    private Button btAccount;
    private static TextView tvTotal, tvNumber;
    private static LinearLayout cartNull;

    public ShoppingCartContent(Context context) {
        this(context, null);
    }

    public ShoppingCartContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_shopping, this);
        initView();//初始化view
        initData();//获取数据
    }

    private void initView() {
        mRecycleView = (ListView) findViewById(R.id.fragment_shopping__recycler);
        selectAll = (CheckBox) findViewById(R.id.fragment_shoppongcart__all_select);
        tvTotal = (TextView) findViewById(R.id.fragment_shopping__total);
        tvNumber = (TextView) findViewById(R.id.item_shoppongcart__all_number);
        btAccount = (Button) findViewById(R.id.fragment_shopping__account);
        cartNull = (LinearLayout) findViewById(R.id.fragment_shopping__null);
        //结算点击事件
        btAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //总数大于0可以提交
                if (Integer.parseInt(tvNumber.getText().toString().substring(1, tvNumber.getText().toString().length() - 2)) > 0) {
                    Application.getInstance().setOrderSelectList(mAdapter.orderSelects);
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), SettlementMessageActivity.class);
                    intent.putExtra(SettlementMessageActivity.TOTAL, tvTotal.getText().toString());
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                } else {

                }
            }
        });
        //判断是否全选
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, listResponses, itemPageResArray, 0, "0");
                    mRecycleView.setAdapter(mAdapter);
                } else {
                    clearUi();
                    mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, listResponses, itemPageResArray, 0, "1");
                    mRecycleView.setAdapter(mAdapter);
                }
            }
        });
        itemPageResArray = new int[]{R.layout.item_shoppingcart, R.layout.list_item_view_page_right};
    }

    //计算总计
    public static void totalMoney(int totla, int type) {
        if (type == 1) {//选中
            Float currentMoney = Float.parseFloat(tvTotal.getText().toString().trim()) + totla;
            tvTotal.setText(currentMoney + "");
        } else if (type == 0) {//取消选中
            Float currentMoney = Float.parseFloat(tvTotal.getText().toString().trim()) - totla;
            tvTotal.setText(currentMoney + "");
        } else if (type == 2) {//全部取消
            tvTotal.setText("0.00");
        }
    }

    //计算件数
    public static void totalNumber(int number, int type) {
        String s = tvNumber.getText().toString().substring(1, tvNumber.getText().toString().length() - 2);
        if (type == 1) {//选中
            int currentNumber = Integer.parseInt(s) + number;
            tvNumber.setText("(" + currentNumber + "件)");
        } else if (type == 0) {//取消选中
            int currentNumber = Integer.parseInt(s) - number;
            tvNumber.setText("(" + currentNumber + "件)");
        } else {//全部取消
            tvNumber.setText("(" + 0 + "件)");
        }

    }

    public static void initData() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.SHOPPINGCART_QUERY, new OkHttpClientManager.ResultCallback<List<ShoppingCartListResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<ShoppingCartListResponse> response) {
                listResponses = response;
                if (listResponses.size() == 0) {
                    cartNull.setVisibility(View.VISIBLE);
                } else {
                    cartNull.setVisibility(View.GONE);
                    clearUi();
                    mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, listResponses, itemPageResArray, 0, "2");
                    mRecycleView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<ShoppingCartListResponse>>() {
        });
    }

    private static void clearUi() {
        tvTotal.setText("0.00");
        tvNumber.setText("(" + 0 + "件)");
        selectAll.setChecked(false);
    }
}
