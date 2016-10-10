package com.humming.ascwg.activity.surprise;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.squareup.picasso.Picasso;
import com.wg.promotion.dto.GifResponse;
import com.wg.promotion.dto.PromotionItemInfoRespones;

public class GiftActivity extends AbstractActivity {

    private TextView title;
    private ImageView bannerImage, soldImage, giftImage, back;
    private TextView soldName, soldEnName, giftName, giftEnName, giftNumber, soldNumber;
    private LinearLayout mCartsub;
    private LinearLayout mCartAdd;
    private GifResponse gifResponse;
    public static String IMAGE_PATH = "image_path";
    private Context context;
    private int soldNum, giftNum;
    private PromotionItemInfoRespones soldItem;
    private PromotionItemInfoRespones gifItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_gift);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        initView();//初始化数据
        initData();
    }

    private void initData() {
        gifResponse = Application.getInstance().getGifResponse();
        if (gifResponse != null) {
            Picasso.with(context).load(getIntent().getStringExtra(IMAGE_PATH)).into(bannerImage);
            soldNum = gifResponse.getSoldNum();
            giftNum = gifResponse.getGifNum();
            soldItem = gifResponse.getSoldItem();
            gifItem = gifResponse.getGifItem();
            soldName.setText(soldItem.getNameCn());
            soldEnName.setText(soldItem.getNameEn());
            giftName.setText(gifItem.getNameCn());
            giftEnName.setText(gifItem.getNameEn());
            soldNumber.setText(soldNum + "");
            giftNumber.setText(giftNum + "");
            Picasso.with(context).load(gifItem.getImageUrl()).into(giftImage);
            Picasso.with(context).load(soldItem.getImageUrl()).into(soldImage);
        } else {

        }
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.settlement_message));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bannerImage = (ImageView) findViewById(R.id.content_promotion_gift__bg);
        soldImage = (ImageView) findViewById(R.id.content_promotion_gift__sold_img);
        giftImage = (ImageView) findViewById(R.id.content_promotion_gift__gift_img);
        soldName = (TextView) findViewById(R.id.content_promotion_gift__sold_name);
        giftName = (TextView) findViewById(R.id.content_promotion_gift__gift_name);
        soldEnName = (TextView) findViewById(R.id.content_promotion_gift__sold_englishname);
        giftEnName = (TextView) findViewById(R.id.content_promotion_gift__gift_englishname);
        giftNumber = (TextView) findViewById(R.id.content_promotion_gift__gift_num);
        soldNumber = (TextView) findViewById(R.id.content_promotion_gift__sold_number);
        mCartsub = (LinearLayout) findViewById(R.id.content_promotion_gift__sold_subtract);
        mCartAdd = (LinearLayout) findViewById(R.id.content_promotion_gift__sold_add);
        mCartsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(soldNumber.getText().toString());
                number--;
                int giftNums = number / soldNum * giftNum;
                giftNumber.setText(giftNums + "");
            }
        });
        mCartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(soldNumber.getText().toString());
                number++;
                int giftNums = number / soldNum * giftNum;
                giftNumber.setText(giftNums + "");
            }
        });
    }
}
