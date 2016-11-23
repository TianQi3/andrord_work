package com.humming.ascwg.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.shoppingcart.SettlementMessageActivity;
import com.humming.ascwg.adapter.CartRecycleAdapter;
import com.humming.ascwg.adapter.RightsCartAdapter;
import com.humming.ascwg.model.OrderSelect;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.order.dto.ShoppingCartList;
import com.wg.order.dto.ShoppingCartListResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zhtq on 16/8/3.
 * <p>
 * 购物车
 */
public class ShoppingCartContent extends LinearLayout {
    private View view;
    public static ListView mRecycleView, mRecycleViewYum;
    private CartRecycleAdapter mAdapter;
    private RightsCartAdapter mRightAdapter;
    private ShoppingCartListResponse listResponses;
    private int[] itemPageResArray;
    private int[] itemPageResArray2;
    public static CheckBox selectAll;
    public static CheckBox selectNormal;
    public static CheckBox selectYum;
    private Button btAccount;
    private static TextView tvTotal, tvNumber;
    private static LinearLayout cartNull;
    private List<ShoppingCartList> normalList;
    private List<ShoppingCartList> yumList;
    public static View normalAll;
    public static View yumAll;
    public ArrayList<OrderSelect> orderSelects;
    public ArrayList<OrderSelect> rightSelects;
    public static boolean someOrAllSelect = true;
    public static boolean AllSelect = true;
    public static ArrayList<OrderSelect> orderSelectArrayList;
    public static ArrayList<OrderSelect> rightsLists;

    public ShoppingCartContent(Context context) {
        this(context, null);
    }

    public ShoppingCartContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_shopping, this);
        normalList = new ArrayList<ShoppingCartList>();
        yumList = new ArrayList<ShoppingCartList>();
        initView();//初始化view
        initData();//获取数据
    }

    private void initView() {
        mRecycleView = (ListView) findViewById(R.id.fragment_shopping__recycler);
        mRecycleViewYum = (ListView) findViewById(R.id.fragment_shopping__recycler_yum);
        selectAll = (CheckBox) findViewById(R.id.fragment_shoppongcart__all_select);
        selectNormal = (CheckBox) findViewById(R.id.fragment_shoppongcart__all_select_normal);
        selectYum = (CheckBox) findViewById(R.id.fragment_shoppongcart__all_select_yum);
        tvTotal = (TextView) findViewById(R.id.fragment_shopping__total);
        tvNumber = (TextView) findViewById(R.id.item_shoppongcart__all_number);
        btAccount = (Button) findViewById(R.id.fragment_shopping__account);
        cartNull = (LinearLayout) findViewById(R.id.fragment_shopping__null);
        normalAll = findViewById(R.id.item_shoppongcart__layout_normal);
        yumAll = findViewById(R.id.item_shoppongcart__layout_yum);
        //结算点击事件
        btAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //总数大于0可以提交
                if (Integer.parseInt(tvNumber.getText().toString().substring(1, tvNumber.getText().toString().length() - 2)) > 0) {
                    Application.getInstance().setOrderSelectList(mAdapter.orderSelects);
                    Application.getInstance().setRightSelectList(mRightAdapter.rightSelects);
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), SettlementMessageActivity.class);
                    intent.putExtra(SettlementMessageActivity.TOTAL, tvTotal.getText().toString());
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                } else {

                }
            }
        });

        //正常全选
        selectNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!someOrAllSelect) {
                    someOrAllSelect = true;
                } else {
                    if (isChecked) {
                        //   tvTotal.setText("0.00");
                        //   tvNumber.setText("(" + 0 + "件)");
                        orderSelectArrayList = getCartData(orderSelectArrayList, "0");
                        if (selectYum.isChecked()) {
                            AllSelect = false;
                            selectAll.setChecked(true);
                        }
                        mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_view_pager_delete, orderSelectArrayList, itemPageResArray, 0, "0");
                        mRecycleView.setAdapter(mAdapter);
                        measureListViewHeight(mRecycleView);

                    } else {
                        orderSelectArrayList = getCartData(orderSelectArrayList, "1");
                        mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_view_pager_delete, orderSelectArrayList, itemPageResArray, 0, "1");
                        mRecycleView.setAdapter(mAdapter);
                        measureListViewHeight(mRecycleView);
                        if (!selectYum.isChecked()) {
                            AllSelect = false;
                            selectAll.setChecked(false);
                        }
                    }
                }
            }
        });
        //yum全选
        selectYum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!someOrAllSelect) {
                    someOrAllSelect = true;
                } else {
                    if (isChecked) {
                        // tvTotal.setText("0.00");
                        // tvNumber.setText("(" + 0 + "件)");
                        rightsLists = getRightsData(rightsLists, "0");
                        if (selectNormal.isChecked()) {
                            AllSelect = false;
                            selectAll.setChecked(true);
                        }
                        mRightAdapter = new RightsCartAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_view_pager_delete, rightsLists, itemPageResArray2, 0, "0");
                        mRecycleViewYum.setAdapter(mRightAdapter);
                        measureListViewHeight(mRecycleViewYum);
                    } else {
                        rightsLists = getRightsData(rightsLists, "1");
                        mRightAdapter = new RightsCartAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_view_pager_delete, rightsLists, itemPageResArray2, 0, "1");
                        mRecycleViewYum.setAdapter(mRightAdapter);
                        measureListViewHeight(mRecycleViewYum);
                        if (!selectNormal.isChecked()) {
                            AllSelect = false;
                            selectAll.setChecked(false);
                        }
                    }
                }


            }
        });
        //判断是否全选
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!AllSelect) {
                    AllSelect = true;
                } else {
                    if (isChecked) {
                        selectYum.setChecked(true);
                        AllSelect = true;
                        selectNormal.setChecked(true);
                        AllSelect = true;
                        //   tvTotal.setText("0.00");
                        //   tvNumber.setText("(" + 0 + "件)");
                        if (orderSelectArrayList.size() > 0) {
                            orderSelectArrayList = getCartData(orderSelectArrayList, "0");
                            mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                                    R.layout.list_item_view_pager_delete, orderSelectArrayList, itemPageResArray, 0, "0");
                            mRecycleView.setAdapter(mAdapter);
                            measureListViewHeight(mRecycleView);
                        }
                        if (rightsLists.size() > 0) {
                            rightsLists = getRightsData(rightsLists, "0");
                            mRightAdapter = new RightsCartAdapter(Application.getInstance().getCurrentActivity(),
                                    R.layout.list_item_view_pager_delete, rightsLists, itemPageResArray2, 0, "0");
                            mRecycleViewYum.setAdapter(mRightAdapter);
                            measureListViewHeight(mRecycleViewYum);
                        }
                    } else {
                        someOrAllSelect = false;
                        selectYum.setChecked(false);
                        someOrAllSelect = false;
                        selectNormal.setChecked(false);
                        //    tvTotal.setText("0.00");
                        //  tvNumber.setText("(" + 0 + "件)");
                        if (orderSelectArrayList.size() > 0) {
                            orderSelectArrayList = getCartData(orderSelectArrayList, "1");
                            mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                                    R.layout.list_item_view_pager_delete, orderSelectArrayList, itemPageResArray, 0, "1");
                            mRecycleView.setAdapter(mAdapter);
                            measureListViewHeight(mRecycleView);
                        }
                        if (rightsLists.size() > 0) {
                            rightsLists = getRightsData(rightsLists, "1");
                            mRightAdapter = new RightsCartAdapter(Application.getInstance().getCurrentActivity(),
                                    R.layout.list_item_view_pager_delete, rightsLists, itemPageResArray2, 0, "1");
                            mRecycleViewYum.setAdapter(mRightAdapter);
                            measureListViewHeight(mRecycleViewYum);
                        }
                    }
                }

            }
        });
        itemPageResArray = new int[]{R.layout.item_shoppingcart, R.layout.list_item_view_page_right};
        itemPageResArray2 = new int[]{R.layout.item_rights_yum, R.layout.list_item_view_page_right};
    }

    //获取平常购物车的数据1
    public ArrayList<OrderSelect> getCartData1(List<ShoppingCartList> items, String types) {
        orderSelects = new ArrayList<OrderSelect>();
        for (ShoppingCartList response : items) {
            OrderSelect orderSelect = new OrderSelect();
            orderSelect.setSelect(false);
            orderSelect.setItemId(response.getItemId());
            orderSelect.setQuantity(response.getQuantity());
            orderSelect.setCostPrice(response.getCostPrice());
            orderSelect.setImageUrl(response.getImageUrl());
            orderSelect.setNameCn(response.getNameCn());
            orderSelect.setNameEn(response.getNameEn());
            orderSelect.setShoppingCartId(response.getShoppingCartId());
            orderSelects.add(orderSelect);
        }
        return orderSelects;
    }

    //获取平常购物车的数据
    public ArrayList<OrderSelect> getCartData(List<OrderSelect> items, String types) {
        for (OrderSelect response : items) {
            if ("0".equals(types)) {//全选
                if (response.isSelect()) {
                    // tvTotal.setText("0.00");
                    // tvNumber.setText("(" + 0 + "件)");
                } else {
                    response.setSelect(true);
                    //  viewHolder.mCartItemRadio.setChecked(true);
                    int selectTotal = (int) (response.getQuantity() * response.getCostPrice());
                    ShoppingCartContent.totalMoney(selectTotal, 1);
                    ShoppingCartContent.totalNumber(response.getQuantity(), 1);
                }

            } else if ("1".equals(types)) {
                response.setSelect(false);
                int selectTotal = (int) (response.getQuantity() * response.getCostPrice());
                //  viewHolder.mCartItemRadio.setChecked(false);
                ShoppingCartContent.totalMoney(selectTotal, 0);
                ShoppingCartContent.totalNumber(response.getQuantity(), 0);
            }

        }
        return (ArrayList<OrderSelect>) items;
    }

    public ArrayList<OrderSelect> getRightsData1(List<ShoppingCartList> items, String types) {
        rightSelects = new ArrayList<OrderSelect>();
        for (ShoppingCartList response : items) {
            OrderSelect orderSelect = new OrderSelect();
            orderSelect.setSelect(false);
            orderSelect.setItemId(response.getItemId());
            orderSelect.setQuantity(response.getQuantity());
            orderSelect.setCostPrice(response.getCostPrice());
            orderSelect.setImageUrl(response.getImageUrl());
            orderSelect.setNameCn(response.getNameCn());
            orderSelect.setNameEn(response.getNameEn());
            orderSelect.setShoppingCartId(response.getShoppingCartId());
            rightSelects.add(orderSelect);
        }
        return rightSelects;
    }

    public ArrayList<OrderSelect> getRightsData(List<OrderSelect> items, String types) {
        for (OrderSelect response : items) {
            if ("0".equals(types)) {//全选
                if (response.isSelect()) {
                    //  tvTotal.setText("0.00");
                    //  tvNumber.setText("(" + 0 + "件)");
                } else {
                    response.setSelect(true);
                    //  viewHolder.mCartItemRadio.setChecked(true);
                    int selectTotal = (int) (response.getQuantity() * response.getCostPrice());
                    ShoppingCartContent.totalMoney(selectTotal, 1);
                    ShoppingCartContent.totalNumber(response.getQuantity(), 1);
                }

            } else if ("1".equals(types)) {
                response.setSelect(false);
                int selectTotal = (int) (response.getQuantity() * response.getCostPrice());
                //  viewHolder.mCartItemRadio.setChecked(false);
                ShoppingCartContent.totalMoney(selectTotal, 0);
                ShoppingCartContent.totalNumber(response.getQuantity(), 0);
            }
        }
        return (ArrayList<OrderSelect>) items;
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

    public void initData() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.SHOPPINGCART_QUERY_NEW, new OkHttpClientManager.ResultCallback<ShoppingCartListResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ShoppingCartListResponse response) {
                listResponses = response;
                normalList = response.getNormalList();
                yumList = response.getYumList();
                if (normalList.size() == 0 && yumList.size() == 0) {
                    cartNull.setVisibility(View.VISIBLE);
                } else {
                    cartNull.setVisibility(View.GONE);
                    clearUi();
                    if (normalList.size() > 0) {
                        normalAll.setVisibility(VISIBLE);
                        orderSelectArrayList = getCartData1(normalList, "2");
                        mAdapter = new CartRecycleAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_view_pager_delete, orderSelectArrayList, itemPageResArray, 0, "2");
                        mRecycleView.setAdapter(mAdapter);
                        measureListViewHeight(mRecycleView);
                    }
                    if (yumList.size() > 0) {
                        yumAll.setVisibility(VISIBLE);
                        rightsLists = getRightsData1(yumList, "2");
                        mRightAdapter = new RightsCartAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_view_pager_delete, rightsLists, itemPageResArray2, 0, "2");
                        mRecycleViewYum.setAdapter(mRightAdapter);
                        measureListViewHeight(mRecycleViewYum);
                    }

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, ShoppingCartListResponse.class);
    }

    private void clearUi() {
        tvTotal.setText("0.00");
        tvNumber.setText("(" + 0 + "件)");
        selectAll.setChecked(false);
    }

    public static void measureListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        final float scale = Application.getInstance().getBaseContext().getResources().getDisplayMetrics().density;
        params.height = (int) (112 * scale * listAdapter.getCount() + 0.5f - 8 * scale);
        listView.setLayoutParams(params);
    }

}
