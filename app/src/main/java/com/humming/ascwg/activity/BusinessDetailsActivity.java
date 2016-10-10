package com.humming.ascwg.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.component.PhotoView;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Ztq on 2016/8/17.
 * 商家信息
 */
public class BusinessDetailsActivity extends AbstractActivity {
    private ImageView callBusiness;//打电话
    private TextView title;
    private ImageView back;
    public static String BUSINESS_IMAGE_DETAIL = "business_image_detail";
    public static String BUSINESS_PHONE = "business_phone";
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(Application.getInstance().getResources().getString(R.string.business_detail));
        mLoading.show();
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        callBusiness = (ImageView) findViewById(R.id.toolbar_call);
        callBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = getIntent().getStringExtra(BUSINESS_PHONE);
                if (!"".equals(phoneNumber)) {
                    TelephonyManager mTelephonyManager = (TelephonyManager) Application.getInstance().getCurrentActivity().getSystemService(TELEPHONY_SERVICE);
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
                    Toast.makeText(Application.getInstance().getCurrentActivity(), "电话为空",Toast.LENGTH_SHORT).show();

                }
            }
        });
        String imagePath = getIntent().getStringExtra(BUSINESS_IMAGE_DETAIL);
        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        Picasso.with(this).load(imagePath).into(mPhotoView);
        mPhotoView.enable();
        //   mLoading.hide();
        mPhotoView.animaFrom(mPhotoView.getInfo());
    }

}
