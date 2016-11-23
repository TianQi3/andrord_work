package com.humming.ascwg.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.ShoppingCartRequest;
import com.humming.ascwg.requestUtils.WinesDetails;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.wg.item.dto.ItemDetailResponse;
import com.youth.banner.Banner;

/**
 * Created by Zhtq on 2016/8/12.
 * 酒的详情
 */
public class WinesInfoActivity extends AbstractActivity {

    private TextView tvSoldPrice;
    private TextView tvBrand;
    private TextView tvCode;
    private TextView tvCountry;
    private TextView tvNameCn;
    private TextView tvNameEn;
    private TextView tvRegion;
    private TextView tvStory;
    private TextView tvType;
    private TextView tvVariety;
    private TextView tvVintage;
    private TextView tvVolumn;
    private TextView tvCostPrice;
    private TextView tvdate;
    private TextView tvStock;
    private Button btAddCart;
    private Button btStory;
    private Button btDetail;
    private LinearLayout llstory;
    private LinearLayout lldetail;
    private Long id = null;
    private TextView title, home;
    private ImageView back, imageView;
    private long itemId;
    private String[] images;
    private Banner banner;
    private Animation mAnimation;
    private int width, height;
    public static String NORMAL_YUM_FLAG = "normal_yum_flag";
    private String flag = "";//0代表正常单  1代表yum
    private int stocks;//库存

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wines_info);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        // height = wm.getDefaultDisplay().getHeight();
        initView();//初始化数据
        getData();//获取数据
    }

    private void initView() {
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.cart_anim);
        imageView = (ImageView) findViewById(R.id.content_wines_info__imageview);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong(Constant.ID);
        flag = bundle.getString(NORMAL_YUM_FLAG);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_backs);
        title.setText(getResources().getString(R.string.detail));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home = (TextView) findViewById(R.id.toolbar_homes);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.getInstance().finishAllActivity();
                Intent intents = new Intent(Application.getInstance().getApplicationContext(), MainActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intents);
            }
        });
        banner = (Banner) findViewById(R.id.content_wines_info__banner);
        //showInventory = bundle.getBoolean(Constant.SHOW_INVENTORY, false);
        btAddCart = (Button) findViewById(R.id.content_product_wines_info__addcart);
        tvBrand = (TextView) findViewById(R.id.content_product_wines_info__brand);
        tvCode = (TextView) findViewById(R.id.content_product_wines_info__code);
        tvCountry = (TextView) findViewById(R.id.content_product_wines_info__country);
        tvNameCn = (TextView) findViewById(R.id.content_product_wines_info__name_cn);
        tvNameEn = (TextView) findViewById(R.id.content_product_wines_info__name_en);
        // tvCostPrice = (TextView) findViewById(R.id.content_product_wines_info__costprice);
        tvSoldPrice = (TextView) findViewById(R.id.content_product_wines_info__soldprice);
        tvStock = (TextView) findViewById(R.id.content_product_wines_info__stock);
        tvRegion = (TextView) findViewById(R.id.content_product_wines_info__region);
        tvStory = (TextView) findViewById(R.id.content_product_wines_info__storytext);
        tvType = (TextView) findViewById(R.id.content_product_wines_info__type);
        tvdate = (TextView) findViewById(R.id.content_product_wines_info__date);
        tvVariety = (TextView) findViewById(R.id.content_product_wines_info__variety);
        // tvVintage = (TextView) findViewById(R.id.content_product_wines_info__vintage);
        tvVolumn = (TextView) findViewById(R.id.content_product_wines_info__volumn);
        btDetail = (Button) findViewById(R.id.content_product_wines_info__detail);
        btStory = (Button) findViewById(R.id.content_product_wines_info__story);
        llstory = (LinearLayout) findViewById(R.id.content_product_wines_info__layoutstory);
        lldetail = (LinearLayout) findViewById(R.id.content_product_wines_info__layoutdetail);
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) banner.getLayoutParams();
        linearParams.width = width;//
        linearParams.height = width;
        banner.setLayoutParams(linearParams);
        imageView.setLayoutParams(linearParams);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (images == null || images.length == 1) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //故事
        btStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lldetail.setVisibility(View.GONE);
                btStory.setTextColor(Application.getInstance().getResources().getColor(R.color.tab_bg));
                btDetail.setTextColor(Application.getInstance().getResources().getColor(R.color.text_gray));
                llstory.setVisibility(View.VISIBLE);
            }
        });
        //详情
        btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llstory.setVisibility(View.GONE);
                btStory.setTextColor(Application.getInstance().getResources().getColor(R.color.text_gray));
                btDetail.setTextColor(Application.getInstance().getResources().getColor(R.color.tab_bg));
                lldetail.setVisibility(View.VISIBLE);
            }
        });
        //加入购物车
        btAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == stocks) {//库存为0
                } else {
                    ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest();
                    shoppingCartRequest.setItemId(itemId);
                    shoppingCartRequest.setQuantity(1);
                    if ("0".equals(flag)) {//正常单
                        shoppingCartRequest.setScType(0);
                    } else if ("1".equals(flag)) {//yum单
                        shoppingCartRequest.setScType(1);
                    } else {
                    }
                    OkHttpClientManager.postAsyn(Config.SHOPPINGCART_ADD_UPDATE, new OkHttpClientManager.ResultCallback<ResponseData>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            showShortToast(info.getInfo());
                        }

                        @Override
                        public void onResponse(final ResponseData response) {
                            showShortToast(getResources().getString(R.string.add_cart_success));
                            imageView.setVisibility(View.VISIBLE);
                            imageView.startAnimation(mAnimation);

                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                        }
                    }, shoppingCartRequest, ResponseData.class);
                }
            }
        });

    }

    private void getData() {
        WinesDetails winesDetails = new WinesDetails();
        winesDetails.setItemId(id);
        OkHttpClientManager.postAsyn(Config.ITEM_QUERY_DETIAL, new OkHttpClientManager.ResultCallback<ItemDetailResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ItemDetailResponse response) {
                images = response.getImages();
                if (images == null) {
                    Picasso.with(Application.getInstance().getCurrentActivity()).load(R.drawable.wines_img).into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                    banner.setVisibility(View.GONE);
                } else if (images.length == 1) {
                    banner.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(Application.getInstance().getCurrentActivity()).load(images[0]).into(imageView);
                } else {
                    banner.setImages(images);
                    imageView.setVisibility(View.GONE);
                }
                stocks = response.getStock();
                if (stocks == 0) {
                    btAddCart.setText(getResources().getString(R.string.out_of_stock));
                }
                itemId = response.getId();
                tvBrand.setText(response.getBrand());
                tvCode.setText(response.getItemCode());
                tvCountry.setText(response.getCountry());
                tvNameCn.setText(response.getNameCn());
                tvNameEn.setText(response.getNameEn());
                tvRegion.setText(response.getRegion());
                tvType.setText(response.getType());
                tvVariety.setText(response.getVariety());
                // tvVintage.setText(response.getVintage());
                tvVolumn.setText(response.getVolumn());
                tvStory.setText(response.getStory());
                //  tvCostPrice.setText("¥"+response.getCostPrice());
                tvSoldPrice.setText("¥" + response.getCostPrice());
                tvdate.setText(getResources().getString(R.string.label_vintage) + response.getVintage());
                tvStock.setText(getResources().getString(R.string.label_stock) + response.getStock() + "");
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, winesDetails, ItemDetailResponse.class);
    }
}
