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
import com.humming.ascwg.adapter.EventDetailsAdapter;
import com.humming.ascwg.component.PhotoView;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.EventPromotionRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

/**
 * Created by Elvira on 2016/8/17.
 * 活动详情
 */
public class EventDetailsActivity extends AbstractActivity {

    private ImageView eventImage;
    private TextView eventName;
    private TextView eventEnglishName;
    private TextView eventDate;
    private TextView eventEnglishDate;
    private TextView eventTime;
    private TextView eventEnglishTime;
    private TextView eventVenue;
    private TextView eventEnglishVenue;
    private TextView eventAddress;
    private TextView eventEnglishAddress;
    private TextView eventPrice;
    private TextView eventEnglishPrice;
    private TextView eventSignUp;//参加
    private RecyclerView listView;
    private EventDetailsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView title;
    private ImageView back;
    private ImageView detailImg;
    public static String EVENT_IMAGE_DETAIL = "event_image_detail";
    public static String EVENT_ID = "event_id";
    public static String EVENT_MY = "event_my";
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(Application.getInstance().getResources().getString(R.string.activity_detail));
        mLoading.show();
        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        eventSignUp = (TextView) findViewById(R.id.toolbar_attend);
        //  detailImg = (ImageView) findViewById(R.id.content_surprise_event__image);
        String imagePath = getIntent().getStringExtra(EVENT_IMAGE_DETAIL);
        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        Picasso.with(this).load(imagePath).into(mPhotoView);
        mPhotoView.enable();
        //   mLoading.hide();
        mPhotoView.animaFrom(mPhotoView.getInfo());

        /*eventImage = (ImageView) findViewById(R.id.content_surprise_event__image);
        eventName = (TextView) findViewById(R.id.content_surprise_event__name);
        eventEnglishName = (TextView) findViewById(R.id.content_surprise_event__english_name);
        eventDate = (TextView) findViewById(R.id.content_surprise_event__date);
        eventEnglishDate = (TextView) findViewById(R.id.content_surprise_event__english_date);
        eventTime = (TextView) findViewById(R.id.content_surprise_event__time);
        eventEnglishTime = (TextView) findViewById(R.id.content_surprise_event__english_time);
        eventVenue = (TextView) findViewById(R.id.content_surprise_event__venue);
        eventEnglishVenue = (TextView) findViewById(R.id.content_surprise_event__english_venue);
        eventAddress = (TextView) findViewById(R.id.content_surprise_event__address);
        eventEnglishAddress = (TextView) findViewById(R.id.content_surprise_event__english_address);
        eventPrice = (TextView) findViewById(R.id.content_surprise_event__price);
        eventEnglishPrice = (TextView) findViewById(R.id.content_surprise_event__english_price);
        eventSignUp = (TextView) findViewById(R.id.content_surprise_event__sign_up);
        listView = (RecyclerView) findViewById(R.id.content_surprise_event__listview);
        linearLayoutManager = new LinearLayoutManager(EventDetailsActivity.this);
        listView.setLayoutManager(linearLayoutManager);*/

        if ("1".equals(getIntent().getStringExtra(EVENT_MY))) {
            eventSignUp.setVisibility(View.INVISIBLE);
        } else {
            eventSignUp.setVisibility(View.VISIBLE);
        }
        eventSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // initAdapter();
    }


//    private void initAdapter() {
//        adapter = new EventDetailsAdapter(initData());
//        listView.setAdapter(adapter);
//        adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(EventDetailsActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    //显示备注
    private void showDialog() {
        View view = LayoutInflater.from(EventDetailsActivity.this).inflate(
                R.layout.popupwindows_remark, null, false);

        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(true);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        View rootview = LayoutInflater.from(EventDetailsActivity.this).inflate(R.layout.activity_surprise_event, null, false);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

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
                    eventRequest.setEventId(Long.parseLong(getIntent().getStringExtra(EVENT_ID)));
                    OkHttpClientManager.postAsyn(Config.EVENT_ATTEND, new OkHttpClientManager.ResultCallback<ResponseData>() {
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
