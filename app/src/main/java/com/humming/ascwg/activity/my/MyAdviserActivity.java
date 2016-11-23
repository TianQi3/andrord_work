package com.humming.ascwg.activity.my;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.wg.user.dto.SalesResponse;
import com.humming.ascwg.service.Error;

/**
 * Created by Elvira on 2016/8/8.
 * 销售顾问
 */
public class MyAdviserActivity extends AbstractActivity {

    private TextView title;
    private ImageView back;
    private ImageView bgImage;
    private ImageView companyImage;
    private ImageView headImage;
    private ImageView callPhone;
    private TextView mobile;
    private TextView phone;
    private TextView email;
    private TextView companyAddress;
    private TextView companyNameEn;
    private TextView companyNameCn;
    private TextView saleName;
    private TextView salePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_advisor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData(this);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(Application.getInstance().getResources().getString(R.string.text_adviser));
        bgImage = (ImageView) findViewById(R.id.content_my_advisor__bgimage);
        companyImage = (ImageView) findViewById(R.id.content_my_advisor__head_image1);
        headImage = (ImageView) findViewById(R.id.content_my_advisor__head_image);
        mobile = (TextView) findViewById(R.id.content_my_advisor__mobile);
        phone = (TextView) findViewById(R.id.content_my_advisor__phone);
        email = (TextView) findViewById(R.id.content_my_advisor__email);
        companyAddress = (TextView) findViewById(R.id.content_my_advisor__address);
        companyNameCn = (TextView) findViewById(R.id.content_my_advisor__company);
        companyNameEn = (TextView) findViewById(R.id.content_my_advisor__english_company);
        saleName = (TextView) findViewById(R.id.content_my_advisor__username);
        salePosition = (TextView) findViewById(R.id.content_my_advisor__position);
        callPhone = (ImageView) findViewById(R.id.content_my_advisor__call_phone);
        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
                View layout = inflater.inflate(R.layout.alert_dialog, null);
                final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
                builder.setView(layout);
                builder.setCancelable(false);
                builder.show();
                TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
                TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
                title.setText(getResources().getString(R.string.call_sales));
                message.setText(getResources().getString(R.string.call) + "   " + phone.getText());
                layout.findViewById(R.id.alert_dialog__cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                layout.findViewById(R.id.alert_dialog__submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //拨打电话
                        String phoneNumber = phone.getText().toString();
                        if (!"".equals(phoneNumber)) {
                            TelephonyManager mTelephonyManager = (TelephonyManager) Application.getInstance().getCurrentActivity().getSystemService(Service.TELEPHONY_SERVICE);
                            int absent = mTelephonyManager.getSimState();
                            if (absent == TelephonyManager.SIM_STATE_ABSENT) {
                                Toast.makeText(Application.getInstance().getCurrentActivity(), "请确认sim卡是否插入或者sim卡暂时不可用！",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent phoneIntent = new Intent(
                                        "android.intent.action.CALL", Uri.parse("tel:"
                                        + phoneNumber));
                                startActivity(phoneIntent);
                            }
                        } else {
                            Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.english),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void initData(final Context context) {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.SALES_QUERY, new OkHttpClientManager.ResultCallback<SalesResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(SalesResponse response) {
                Picasso.with(context).load(response.getBgImage()).into(bgImage);
                Picasso.with(context).load(response.getCompanyImage()).into(companyImage);
                Picasso.with(context).load(response.getHeadImage()).into(headImage);
                email.setText(response.getEmail());
                saleName.setText(response.getSaleName());
                salePosition.setText(response.getSalePosition());
                phone.setText(response.getMobile());
                mobile.setText(response.getPhone());
                companyNameEn.setText(response.getCompanyNameEn());
                companyNameCn.setText(response.getCompanyNameCn());
                companyAddress.setText(response.getCompanyAddress());
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, SalesResponse.class);
    }

}
