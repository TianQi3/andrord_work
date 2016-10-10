package com.humming.ascwg.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.adapter.PromotionDetailsAdapter;
import com.humming.ascwg.component.PhotoView;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.EventPromotionRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

/**
 * Created by Elvira on 2016/8/16.
 * 促销详细
 */
public class PromotionDetailsActivity extends AbstractActivity {

    private ImageView wineImage;
    private TextView wineName;
    private TextView wineEnglishName;
    private TextView winePrice;
    private TextView addShopping;
    private RecyclerView listView;
    private PromotionDetailsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView title;
    private ImageView back;
    public static String PROMOTION_IMAGE_DETAIL = "promotion_image_detail";
    public static String PROMOTION_ID = "promotion_id";
    public static String PROMOTION_MY = "promotion_my";
    private PhotoView mPhotoView;
    private TextView promotionSignUp;//参加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_promotion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(Application.getInstance().getResources().getString(R.string.promotion_detail));
        mLoading.show();
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPhotoView = (PhotoView) findViewById(R.id.content_surprise_promotion__photo_view);
        promotionSignUp = (TextView) findViewById(R.id.toolbar_attend);
        if ("1".equals(getIntent().getStringExtra(PROMOTION_MY))) {
            promotionSignUp.setVisibility(View.INVISIBLE);
        } else {
            promotionSignUp.setVisibility(View.VISIBLE);
        }
        promotionSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        String imagePath = getIntent().getStringExtra(PROMOTION_IMAGE_DETAIL);
        Picasso.with(this).load(imagePath).into(mPhotoView);
        mPhotoView.enable();
        mPhotoView.animaFrom(mPhotoView.getInfo());

//        wineImage = (ImageView) findViewById(R.id.content_surprise_promotion__image);
//        wineName = (TextView) findViewById(R.id.content_surprise_promotion__wine_name);
//        wineEnglishName = (TextView) findViewById(R.id.content_surprise_promotion__wine_english_name);
//        winePrice = (TextView) findViewById(R.id.content_surprise_promotion__wine_price);
//        addShopping = (TextView) findViewById(R.id.content_surprise_promotion__add_shopping);
//        listView = (RecyclerView) findViewById(R.id.content_surprise_promotion__listview);
//        linearLayoutManager = new LinearLayoutManager(PromotionDetailsActivity.this);
//        listView.setLayoutManager(linearLayoutManager);
//        initAdapter();
    }

    //显示备注
    private void showDialog() {
        View view = LayoutInflater.from(PromotionDetailsActivity.this).inflate(
                R.layout.popupwindows_remark, null, false);

        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        View rootview = LayoutInflater.from(PromotionDetailsActivity.this).inflate(R.layout.activity_surprise_event, null, false);
      //  mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
     //   mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER_VERTICAL, 0, 0);

        TextView cancel = (TextView) view.findViewById(R.id.remark__cancel);
        TextView submit = (TextView) view.findViewById(R.id.remark_submit);
        final EditText remarkContent = (EditText) view.findViewById(R.id.remark_content);
        final EditText phoneContent = (EditText) view.findViewById(R.id.phone_content);
        final EditText contactContent = (EditText) view.findViewById(R.id.contact_content);
        String phone = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PHONE, "", Application.getInstance().getCurrentActivity());
        String contract = SharePrefUtil.getString(Constant.FILE_NAME, Constant.CONTRACT, "", Application.getInstance().getCurrentActivity());
        phoneContent.setText(phone);
        contactContent.setText(contract);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                mPopupWindow.dismiss();
            }
        });
        //参加
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                if (checkValue()) {//又空值
                    showShortToast(getResources().getString(R.string.information_is_not_perfect));
                } else {
                    String remark = remarkContent.getText().toString();
                    EventPromotionRequest eventRequest = new EventPromotionRequest();
                    eventRequest.setRemark(remark);
                    eventRequest.setContact(contactContent.getText().toString());
                    eventRequest.setPhone(phoneContent.getText().toString());
                    eventRequest.setEventId(Long.parseLong(getIntent().getStringExtra(PROMOTION_ID)));
                    OkHttpClientManager.postAsyn(Config.PROMOTION_ATTEND, new OkHttpClientManager.ResultCallback<ResponseData>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            showShortToast(info.getInfo());
                        }

                        @Override
                        public void onResponse(ResponseData response) {
                            showShortToast(getResources().getString(R.string.attend_success));
                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                        }
                    }, eventRequest, ResponseData.class);
                    mPopupWindow.dismiss();
                }

            }

            //检查是否为空
            private boolean checkValue() {
                boolean check = false;
                if ("".equals(contactContent.getText().toString())) {
                    check = true;
                } else if ("".equals(phoneContent.getText().toString())) {
                    check = true;
                }
                return check;
            }
        });

    }
}
