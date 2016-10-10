package com.humming.ascwg.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.SetInformationActivity;
import com.humming.ascwg.activity.my.MyAddressActivity;
import com.humming.ascwg.activity.my.MyAdviserActivity;
import com.humming.ascwg.activity.my.MyBillActivity;
import com.humming.ascwg.activity.my.MyEventActivity;
import com.humming.ascwg.activity.my.MyOrderActivity;
import com.humming.ascwg.component.CircleImageView;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Zhtq on 16/8/3.
 * <p/>
 * 我的
 */
public class MyContent extends LinearLayout implements View.OnClickListener {
    private View view;
    private CircleImageView headImage;//头像
    private TextView userName;
    private LinearLayout settingLayout;//设置
    private LinearLayout adviserLayout;//销售顾问
    private LinearLayout orderLayout;//历史订单
    private LinearLayout addressLayout;//地址管理
    private LinearLayout eventLayout;//活动
    private LinearLayout billLayout;//发票管理
    private Context context;

    public MyContent(Context context) {
        this(context, null);
    }

    public MyContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_my, this);
        this.context = context;
        headImage = (CircleImageView) view.findViewById(R.id.fragment_my__head_img);
        userName = (TextView) view.findViewById(R.id.fragment_my__username);
        settingLayout = (LinearLayout) view.findViewById(R.id.fragment_my__setting);
        adviserLayout = (LinearLayout) view.findViewById(R.id.fragment_my__adviser);
        orderLayout = (LinearLayout) view.findViewById(R.id.fragment_my__order);
        addressLayout = (LinearLayout) view.findViewById(R.id.fragment_my__address);
        eventLayout = (LinearLayout) view.findViewById(R.id.fragment_my__event);
        billLayout = (LinearLayout) view.findViewById(R.id.fragment_my__bill);
        String userNames = SharePrefUtil.getString(Constant.FILE_NAME, Constant.CONTRACT, "", Application.getInstance().getCurrentActivity());
        String headImages = SharePrefUtil.getString(Constant.FILE_NAME, Constant.HEAD_IMAGE, "", Application.getInstance().getCurrentActivity());
        Picasso.with(context).load(headImages).into(headImage);
        userName.setText(userNames);
        headImage.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        adviserLayout.setOnClickListener(this);
        orderLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        eventLayout.setOnClickListener(this);
        billLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_my__head_img:
                Intent intent1 = new Intent(Application.getInstance().getCurrentActivity(), SetInformationActivity.class);
                intent1.putExtra(SetInformationActivity.COME_FROM_MY, "true");
                Application.getInstance().getCurrentActivity().startActivityForResult(intent1, SetInformationActivity.NAME_IMAGE_RESULT_CODE);
                break;
            case R.id.fragment_my__setting:
                break;
            case R.id.fragment_my__adviser:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), MyAdviserActivity.class));
                break;
            case R.id.fragment_my__order:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), MyOrderActivity.class));
                break;
            case R.id.fragment_my__address:
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyAddressActivity.class);
                intent.putExtra(MyAddressActivity.ADDRESS_TYPE, "1");
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_my__event:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), MyEventActivity.class));
                break;
            case R.id.fragment_my__bill:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), MyBillActivity.class));
                break;
        }
    }

    public void refreshUI(String name, String imagePath) {
        Picasso.with(context).load(imagePath).into(headImage);
        userName.setText(name);
        SharePrefUtil.putString(Constant.FILE_NAME, Constant.CONTRACT, name, Application.getInstance().getCurrentActivity());
        SharePrefUtil.putString(Constant.FILE_NAME, Constant.HEAD_IMAGE, imagePath, Application.getInstance().getCurrentActivity());
    }
}
