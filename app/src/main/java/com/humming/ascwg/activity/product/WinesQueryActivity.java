package com.humming.ascwg.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Constant;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.R;

public class WinesQueryActivity extends AbstractActivity {

    private EditText etItemCode;
    private EditText etBrand;
    private EditText etCountry;
    private EditText etName;
    private EditText etType;
    private String itemCode = "";
    private String brandEN = "";
    private String countryEN = "";
    private String typeEN = "";
    private Button btSubmit;
    private Button btReset;
    private Context context;
    private String brandId = "";
    private String countryId = "";
    private String itemTypeId = "";
    private TextView title;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wines_serach);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        initView();//初始化数据
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.search));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etItemCode = (EditText) findViewById(R.id.activity_product_wines_search__item_code);
        etBrand = (EditText) findViewById(R.id.activity_product_wines_search__brand);
        etCountry = (EditText) findViewById(R.id.activity_product_wines_search__country);
        etName = (EditText) findViewById(R.id.activity_product_wines_search__name);
        etType = (EditText) findViewById(R.id.activity_product_wines_search__type);
        btSubmit = (Button) findViewById(R.id.activity_product_wines_search__btn_submit);
        btReset = (Button) findViewById(R.id.activity_product_wines_search__btn_reset);
        etItemCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showItemCodeSelector();
            }
        });

        etBrand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showBrandSelector();
            }
        });

        etCountry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showCountrySelector();
            }
        });

        etType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showWinesTypeSelector();
            }
        });

        btSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (name.length() == 0 && itemCode.length() == 0
                        && brandEN.length() == 0 && countryEN.length() == 0
                        && typeEN.length() == 0) {
                    Toast.makeText(context, R.string.msg_at_least_one_item,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, WinesListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.AS_QUERY_RESULT, true);
                bundle.putString(Constant.BRAND, brandId);
                bundle.putString(Constant.NAME, name);
                bundle.putString(Constant.TYPE, itemTypeId);
                bundle.putString(Constant.ITEM_CODE, itemCode);
                bundle.putString(Constant.COUNTRY, countryId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                etName.setText("");
                resetItemCode();
                resetBrand();
                resetCountry();
                resetType();
            }
        });
    }

    //itemCode
    private void showItemCodeSelector() {
        resetItemCode();
        Intent intent = new Intent(context, ItemCodeSelectorActivity.class);
        startActivityForResult(intent,
                Constant.ACTIVITY_RESULT_ITEM_CODE_SELECTED);

    }

    private void resetItemCode() {
        itemCode = "";
        etItemCode.setText(itemCode);
    }

    private void showBrandSelector() {
        resetBrand();
        Intent intent = new Intent(context, BrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.AS_SELECTOR, true);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constant.ACTIVITY_RESULT_BRAND_SELECTED);
    }

    private void resetBrand() {
        brandEN = "";
        brandId = "";
        etBrand.setText(brandEN);
    }

    private void showCountrySelector() {
        resetCountry();
        Intent intent = new Intent(context, CountryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.AS_SELECTOR, true);
        intent.putExtras(bundle);
        startActivityForResult(intent,
                Constant.ACTIVITY_RESULT_COUNTRY_SELECTED);
    }

    private void resetCountry() {
        countryEN = "";
        countryId = "";
        etCountry.setText(countryEN);
    }

    private void showWinesTypeSelector() {
        resetType();
        Intent intent = new Intent(context, TypeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.AS_SELECTOR, true);
        intent.putExtras(bundle);
        startActivityForResult(intent,
                Constant.ACTIVITY_RESULT_WINES_TYPE_SELECTED);
    }

    private void resetType() {
        typeEN = "";
        itemTypeId = "";
        etType.setText(typeEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        switch (requestCode) {
            case Constant.ACTIVITY_RESULT_ITEM_CODE_SELECTED:
                itemCode = resultBundle.getString(Constant.ITEM_CODE);
                etItemCode.setText(itemCode);
                break;
            case Constant.ACTIVITY_RESULT_BRAND_SELECTED:
                brandEN = resultBundle.getString(Constant.EN);
                brandId = resultBundle.getString(Constant.ID);
                String brandCN = resultBundle.getString(Constant.CN);
                etBrand.setText(brandEN + " " + brandCN);
                break;
            case Constant.ACTIVITY_RESULT_COUNTRY_SELECTED:
                countryEN = resultBundle.getString(Constant.EN);
                countryId = resultBundle.getString(Constant.ID);
                String countryCN = resultBundle.getString(Constant.CN);
                etCountry.setText(countryEN + " " + countryCN);
                break;
            case Constant.ACTIVITY_RESULT_WINES_TYPE_SELECTED:
                typeEN = resultBundle.getString(Constant.EN);
                itemTypeId = resultBundle.getString(Constant.ID);
                String typeCN = resultBundle.getString(Constant.CN);
                etType.setText(typeEN + " " + typeCN);
                break;
        }
    }

}
