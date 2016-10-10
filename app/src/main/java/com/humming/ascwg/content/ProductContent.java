package com.humming.ascwg.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.humming.ascwg.Application;
import com.humming.ascwg.activity.product.BrandActivity;
import com.humming.ascwg.activity.product.CountryActivity;
import com.humming.ascwg.activity.product.TypeActivity;
import com.humming.ascwg.activity.product.WinesQueryActivity;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.R;


/**
 * Created by Zhtq on 16/8/3.
 * <p/>
 * 产品
 */
public class ProductContent extends LinearLayout {
    private View view;
    protected Context context;
    private View vContry;
    private View vType;
    private View vBrand;
    private View vSearch;

    public ProductContent(Context context) {
        this(context, null);
    }

    public ProductContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = inflate(context, R.layout.fragment_product, this);
        vContry = findViewById(R.id.content_product__country);
        vType = findViewById(R.id.content_product__type);
        vBrand = findViewById(R.id.content_product__brand);
        vSearch = findViewById(R.id.content_product__search);
        vContry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startCountryActivity();
            }
        });

        vType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startTypeActivity();
            }
        });

        vBrand.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startBrandActivity();
            }
        });

        vSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startSearchActivity();
            }
        });
    }

    RequestNull requestNull = new RequestNull();

    //国家
    private void startCountryActivity() {
        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), CountryActivity.class);
        Application.getInstance().getCurrentActivity().startActivity(intent);
        Application.getInstance().getCurrentActivity().overridePendingTransition(R.anim.fade_in2, R.anim.fade_out2);
    }

    private void startTypeActivity() {
        Intent intent = new Intent(context, TypeActivity.class);
        Application.getInstance().getCurrentActivity().startActivity(intent);

    }

    private void startBrandActivity() {
        Intent intent = new Intent(context, BrandActivity.class);
        Application.getInstance().getCurrentActivity().startActivity(intent);

    }

    private void startSearchActivity() {
        Intent intent = new Intent(context, WinesQueryActivity.class);
        Application.getInstance().getCurrentActivity().startActivity(intent);
    }

}
