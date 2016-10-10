package com.humming.ascwg.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.AddressRequest;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.service.Error;
import com.squareup.okhttp.Request;

;


/**
 * Created by Zhtq on 2016/8/8.
 * 地址管理
 */
public class AddressMessageActivity extends AbstractActivity {
    private TextView title, save;
    private ImageView back;
    private EditText mConsignee, mContact, mAddress;
    private CheckBox mdefault;
    public static final int ACTIVITY_ADDRESS_RESULT = 1001;
    public static final String KEY_TEXT = "address_text";
    public static final String UPDATE_OR_ADD = "update_or_add";
    public static final String CONSIGNEE = "consignee";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "address";
    public static final String SHIPPING_ID = "shipping_id";
    private String shippingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_message);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();//初始化数据
    }

    private void initView() {
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        save = (TextView) findViewById(R.id.toolbar_commit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        AddressMessageActivity.KEY_TEXT,
                        "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        ACTIVITY_ADDRESS_RESULT,
                        resultIntent);
                finish();
            }
        });
        mConsignee = (EditText) findViewById(R.id.content_address_message__consignee);
        mContact = (EditText) findViewById(R.id.content_address_message__contact_infomation);
        mAddress = (EditText) findViewById(R.id.content_address_message__address);
        mdefault = (CheckBox) findViewById(R.id.content_address_message__defult);

        if ("0".equals(getIntent().getStringExtra(UPDATE_OR_ADD))) {//修改
            title.setText(getResources().getString(R.string.update_address));
            mConsignee.setText(getIntent().getStringExtra(CONSIGNEE));
            mContact.setText(getIntent().getStringExtra(CONTACT));
            mAddress.setText(getIntent().getStringExtra(ADDRESS));
            shippingId = getIntent().getStringExtra(SHIPPING_ID);
        } else {//添加
            title.setText(getResources().getString(R.string.add_address));
        }
        //保存点击事件
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (judgeNull()) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), Application.getInstance().getResources().getString(R.string.please_fill_in_the_complete), Toast.LENGTH_SHORT).show();
                } else {
                    AddressRequest addressRequest = new AddressRequest();
                    if ("0".equals(getIntent().getStringExtra(UPDATE_OR_ADD))) {
                        addressRequest.setShippingAddressId(Long.parseLong(shippingId));
                    }
                    addressRequest.setContact(mConsignee.getText().toString());
                    addressRequest.setPhone(mContact.getText().toString());
                    addressRequest.setDetailAddress(mAddress.getText().toString());
                    if (mdefault.isChecked()) {
                        addressRequest.setDefaultFlg(1);
                    } else {
                        addressRequest.setDefaultFlg(0);
                    }
                    OkHttpClientManager.postAsyn(Config.ADDRESS_SAVE_UPDATE, new OkHttpClientManager.ResultCallback<ResponseData>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            showShortToast(info.getInfo());
                        }

                        @Override
                        public void onResponse(ResponseData response) {
                            Toast.makeText(Application.getInstance().getCurrentActivity(), Application.getInstance().getResources().getString(R.string.add_address_success), Toast.LENGTH_SHORT).show();
                            Bundle resultBundle = new Bundle();
                            resultBundle.putString(
                                    AddressMessageActivity.KEY_TEXT,
                                    "1");
                            Intent resultIntent = new Intent()
                                    .putExtras(resultBundle);
                            setResult(
                                    ACTIVITY_ADDRESS_RESULT,
                                    resultIntent);
                            finish();
                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                        }
                    }, addressRequest, ResponseData.class);
                }
            }
        });
    }

    private boolean judgeNull() {
        boolean isture = false;
        if (!"".equals(mConsignee.getText().toString())) {

        } else {
            isture = true;
        }
        if (!"".equals(mAddress.getText().toString())) {

        } else {
            isture = true;
        }
        if (!"".equals(mContact.getText().toString())) {

        } else {
            isture = true;
        }
        return isture;
    }
}
