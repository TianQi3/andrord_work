package com.humming.ascwg.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.MainActivity;
import com.humming.ascwg.R;
import com.humming.ascwg.component.CircleImageView;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.model.TextEditorData;
import com.humming.ascwg.requestUtils.BaseInformation;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.requestUtils.UserInformation;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.humming.ascwg.service.RequestData;
import com.humming.ascwg.utils.BitmapUtils;
import com.humming.ascwg.utils.FileUtils;
import com.humming.ascwg.utils.SharePrefUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.wg.user.dto.SelfInfoResponse;
import com.wg.user.dto.UpdateResponse;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Zhtq on 2016/5/12.
 * 设置用户信息
 */
public class SetInformationActivity extends BasePhotoActivity implements View.OnClickListener {

    private View addressLayout, birthdayLayout, nickLayout;
    private TextView addressText, birthdayText, nickText;
    private int year, monthOfYear, dayOfMonth;
    private LinearLayout sexLayout;
    private LinearLayout parent;
    private TextView sexTv, confirm;
    private CircleImageView headImage;
    private Dialog dialog;
    private RadioButton man;
    private RadioButton woman;
    private TextEditorData textEditorData;
    private ImageView back;
    public String KEY_IMAGE = "fileName";
    public String KEY_NAME = "files";
    private Switch bankCloseOpen, pumCloseOpen;
    private EditText bankCode, pumCode;
    private TextView title;
    private Handler mDelivery;
    private TextView pumText, bankText;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    public static String COME_FROM_MY = "come_from_my";
    public static final int NAME_IMAGE_RESULT_CODE = 10015;
    public static final String NAME_RESULT = "name_result";
    public static final String IMAGE_RESULT = "image_result";
    private View bankCodeLayout, yumCodeLayout;
    private TextView bankCommit, yumCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_information);
        mDelivery = new Handler(Looper.getMainLooper());
        parent = (LinearLayout) findViewById(R.id.activity_set_information__parent);
        title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.toolbar_back);
        title.setText(getResources().getString(R.string.my_message));
        addressLayout = findViewById(R.id.activity_set_information__address_layout);
        addressText = (TextView) findViewById(R.id.activity_set_information__address);
        birthdayLayout = findViewById(R.id.activity_set_information__birthday_layout);
        birthdayText = (TextView) findViewById(R.id.activity_set_information__birthday);
        birthdayLayout.setOnClickListener(this);
        nickLayout = findViewById(R.id.activity_set_information__nick_layout);
        nickLayout.setOnClickListener(this);
        nickText = (TextView) findViewById(R.id.activity_set_information__nick);
        sexLayout = (LinearLayout) findViewById(R.id.activity_set_information__sex_layout);
        sexTv = (TextView) findViewById(R.id.activity_set_information__sex);
        headImage = (CircleImageView) findViewById(R.id.activity_set_information__head_img);
        bankCloseOpen = (Switch) findViewById(R.id.activity_set_information__switch);
        bankCode = (EditText) findViewById(R.id.activity_set_information__bank_code);
        pumCloseOpen = (Switch) findViewById(R.id.activity_set_information__switch_pum);
        pumCode = (EditText) findViewById(R.id.activity_set_information__yum_code);
        pumText = (TextView) findViewById(R.id.activity_set_information__yum_text);
        bankText = (TextView) findViewById(R.id.activity_set_information__bank_text);
        bankCodeLayout = findViewById(R.id.activity_set_information__bank_code_layout);
        yumCodeLayout = findViewById(R.id.activity_set_information__yum_code_layout);
        bankCommit = (TextView) findViewById(R.id.activity_set_information__bank_code_commit);
        yumCommit = (TextView) findViewById(R.id.activity_set_information__yum_code_commit);
        String pum = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PUM_CODE, "", SetInformationActivity.this);//判断是否验证过百盛会员
        String bank = SharePrefUtil.getString(Constant.FILE_NAME, Constant.BANK_CODE, "", SetInformationActivity.this);//判断是否验证过银行会员
        if (pum != null && !"".equals(pum)) {
            yumCodeLayout.setVisibility(View.GONE);
            pumCloseOpen.setVisibility(View.GONE);
            pumText.setText(getResources().getString(R.string.pum_code_verified));
        }
        if (bank != null && !"".equals(bank)) {
            bankCodeLayout.setVisibility(View.GONE);
            bankCloseOpen.setVisibility(View.GONE);
            bankText.setText(getResources().getString(R.string.bank_code_verified));
        }
        confirm = (TextView) findViewById(R.id.activity_set_information__confirm);
        if ("true".equals(getIntent().getStringExtra(COME_FROM_MY))) {//从我的里面进来。需要获取个人信息
            getSelfInfo();
        } else {
        }
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        headImage.setOnClickListener(this);
        yumCommit.setOnClickListener(this);
        bankCommit.setOnClickListener(this);
        bankCloseOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//表示选中状态
                    bankCodeLayout.setVisibility(View.VISIBLE);
                } else {//未选中
                    bankCodeLayout.setVisibility(View.GONE);
                }
            }
        });
        pumCloseOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//表示选中状态
                    yumCodeLayout.setVisibility(View.VISIBLE);
                } else {//未选中
                    yumCodeLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //获取个人信息
    private void getSelfInfo() {
        RequestNull requerstNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.USER_SELFINFO, new OkHttpClientManager.ResultCallback<SelfInfoResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(SelfInfoResponse response) {
                Picasso.with(Application.getInstance().getCurrentActivity()).load(response.getHeadImage()).into(headImage);
                nickText.setText(response.getName() + "");
                sexTv.setText(response.getSex() + "");
                birthdayText.setText(response.getBirthday() + "");
                addressText.setText(response.getArea() + "");
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requerstNull, SelfInfoResponse.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //所在地点击事件
            case R.id.activity_set_information__address_layout:
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), AreaSelectActivity.class);
                startActivityForResult(intent, AreaSelectActivity.ACTIVITY_AREA_RESULT);
                break;
            //生日
            case R.id.activity_set_information__birthday_layout:
                showPopWindowDatePicker();
                break;
            //性别
            case R.id.activity_set_information__sex_layout:
                checkSexDialog();
                break;
            case R.id.activity_set_information__head_img://选择头像
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                new PopupWindows(SetInformationActivity.this, parent);
                break;
            case R.id.activity_set_information__nick_layout:
                TextEdit(TextEditorActivity.ACTIVITY_CHANGE_NAME, nickText, Application.getInstance().getString(R.string.change_name));
                break;
            case R.id.dialog_sex__man:
                man.setChecked(true);
                woman.setChecked(false);
                sexTv.setText(getResources().getString(R.string.sex_man));
                dialog.dismiss();
                break;
            case R.id.dialog_sex__woman:
                woman.setChecked(true);
                man.setChecked(false);
                sexTv.setText(getResources().getString(R.string.sex_woman));
                dialog.dismiss();
                break;
            case R.id.activity_set_information__bank_code_commit://银行会员
                CheckBandCode();
                break;
            case R.id.activity_set_information__yum_code_commit://yum 会员
                CheckPumCode();
                break;
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.activity_set_information__confirm:
                VerificationInformation();
                break;
        }

    }

    //验证银行码--->验证个人信息
    private void CheckBandCode() {
        BaseInformation base = new BaseInformation();
        base.setVipCode(bankCode.getText().toString());
        OkHttpClientManager.postAsyn(Config.CHECK_VIP_CODE, new OkHttpClientManager.ResultCallback<ResponseData>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ResponseData response) {
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.BANK_CODE, bankCode.getText().toString(), SetInformationActivity.this);
                bankCodeLayout.setVisibility(View.GONE);
                bankCloseOpen.setVisibility(View.GONE);
                bankText.setText(getResources().getString(R.string.bank_code_verified));
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, base, ResponseData.class);
    }

    //百盛验证码  --> 验证
    private void CheckPumCode() {
        BaseInformation base = new BaseInformation();
        base.setYumCode(pumCode.getText().toString());
        OkHttpClientManager.postAsyn(Config.CHECK_YUM_CODE, new OkHttpClientManager.ResultCallback<ResponseData>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ResponseData response) {
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PUM_CODE, pumCode.getText().toString(), SetInformationActivity.this);
                yumCodeLayout.setVisibility(View.GONE);
                pumCloseOpen.setVisibility(View.GONE);
                pumText.setText(getResources().getString(R.string.pum_code_verified));
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, base, ResponseData.class);
    }

    //验证个人信息
    private void VerificationInformation() {
        UserInformation userInformation = new UserInformation();
        userInformation.setArea(addressText.getText().toString());
        userInformation.setBirthday(birthdayText.getText().toString());
        userInformation.setName(nickText.getText().toString());
        userInformation.setSex(sexTv.getText().toString());
        // userInformation.setSignature(signatureEdit.getText().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String parameters = objectMapper.writeValueAsString(userInformation);
            String ss = "\"" + "{}" + "\"";
            OkHttpClientManager.postAsyn(Config.USER_INFORMATION, new OkHttpClientManager.ResultCallback<UpdateResponse>() {
                @Override
                public void onError(Request request, Error info) {
                    Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                    showShortToast(info.getInfo());
                }

                @Override
                public void onResponse(UpdateResponse response) {
                    Log.v("xxxxxxx", response.getHeadImage());
                    showShortToast(getResources().getString(R.string.setting_success));
                    if ("true".equals(getIntent().getStringExtra(COME_FROM_MY))) {
                        Bundle resultBundle = new Bundle();
                        resultBundle.putString(
                                SetInformationActivity.NAME_RESULT,
                                response.getName());
                        resultBundle.putString(
                                SetInformationActivity.IMAGE_RESULT,
                                response.getHeadImage());
                        Intent resultIntent = new Intent()
                                .putExtras(resultBundle);
                        setResult(
                                NAME_IMAGE_RESULT_CODE,
                                resultIntent);
                        finish();
                    } else {
                        Intent i = new Intent(Application.getInstance().getCurrentActivity(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onOtherError(Request request, Exception exception) {
                    Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                }
            }, userInformation, UpdateResponse.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void TextEdit(int result, TextView textview, String title) {
        textEditorData = new TextEditorData();
        textEditorData.setTitle(title);
        textEditorData.setHint(Application.getInstance().getBaseContext().getString(R.string.please_enter_name));
        textEditorData.setSingleLine(true);
        Application.getInstance().setTextEditorData(textEditorData);
        Intent editeIntent2 = new Intent(getBaseContext(), TextEditorActivity.class);
        if (!"".equals(textview.getText())) {
            editeIntent2.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, textview.getText());
        }
        startActivityForResult(editeIntent2, result);
    }

    //时间的popwindow
    //popwindow显示
    private void showPopWindowDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.activity_set_information__address_layout),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int years,
                                      int monthOfYears, int dayOfMonths) {
                year = years;
                monthOfYear = monthOfYears;
                dayOfMonth = dayOfMonths;
            }

        });
        TextView dateCancel = (TextView) view.findViewById(R.id.date_cancel);
        TextView datesubmit = (TextView) view.findViewById(R.id.date_submit);
        dateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        datesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = "";
                String day = "";
                String date = "";
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                if (monthOfYear < 9) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                date = year + "-" + month + "-" + day;
                birthdayText.setText(date);
                window.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle resultBundle = null;
        switch (requestCode) {
            case AreaSelectActivity.ACTIVITY_AREA_RESULT:
                resultBundle = data.getExtras();
                String text = resultBundle.getString(AreaSelectActivity.KEY_TEXT);
                if ("".endsWith(text)) {

                } else {
                    addressText.setText(text);
                }
                break;
            case TextEditorActivity.ACTIVITY_CHANGE_NAME:
                resultBundle = data.getExtras();
                String texts = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                nickText.setText(texts);
                break;
        }
        if (!mIsKitKat) {//低于4.4的版本
            switch (requestCode) {
                case CODE_CAMERA_REQUEST_TWO://调用相机返回
                    if (resultCode == RESULT_OK) {
                        updateImage(mPublishPhotoPath);
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
                case CODE_GALLERY_REQUEST_TWO://调用相册返回

                    if (resultCode == RESULT_CANCELED) {
                        showShortToast("取消了选择图片");
                    } else if (resultCode == RESULT_OK) {
                        if (data != null && data.getData() != null) {
                            String path = getPhotoName(data.getData(), true, "");
                            updateImage(path);
                        }
                    }
                    break;
            }
        } else {//高于4.4
            switch (requestCode) {
                case CODE_CAMERA_REQUEST_ONE:
                    if (resultCode == RESULT_OK) {
                        updateImage(mPublishPhotoPath);
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
                case CODE_GALLERY_REQUEST_ONE:

                    if (resultCode == RESULT_CANCELED) {
                        showShortToast("取消了选择图片");
                    } else if (resultCode == RESULT_OK) {
                        if (data != null && data.getData() != null) {
                            String path = FileUtils.getPath(this, data.getData());
                            updateImage(path);
                        }
                    }
                    break;
            }
        }
    }

    //更新头像
    private void updateImage(String path) {
        try {
            headImage.setImageBitmap(BitmapUtils.revitionImageSize(path));
            upLoadImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //上传图片
    private void upLoadImage(String path) {
        final File file = new File(path);
        String image = BitmapUtils.getStringImage(BitmapUtils.convertToBitmap(path, 130, 130));
        String name = file.getName();

        //Creating parameters
        Map<String, String> params = new Hashtable<String, String>();
        params.put(KEY_IMAGE, image);
        params.put(KEY_NAME, name);
        OkHttpClient client = new OkHttpClient();
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (String key : params.keySet()) {
            //   builder.addFormDataPart(key, params.get(key));
        }
        builder.addFormDataPart("files", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", Application.getInstance().getCurrentActivity());
        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("cmd", Config.URL_SERVICE_ALIIMAGE);
        /*requestData.setCmd(Config.URL_SERVICE_ALIIMAGE);
        UserInformation userInformation = new UserInformation();
        userInformation.setArea(addressText.getText().toString());
        userInformation.setBirthday(birthdayText.getText().toString());
        userInformation.setName(nickText.getText().toString());
        userInformation.setSex(sexTv.getText().toString());
        requestData.setParameters(userInformation);
        String json = new Gson().toJson(requestData);
        RequestBody requestBody1 = RequestBody.create(JSON, json);*/
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(Config.URL_SERVICE)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Request request, IOException e) {
                                                Log.v("xxxxxx", "+++" + e.getMessage());
                                                mDelivery.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(Application.getInstance().getCurrentActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(Response response) throws IOException {
                                                Log.v("xxxxxx", "response = " + response.body().string());
                                                ObjectMapper mapper = new ObjectMapper();
                                                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                                try {
                                                    final String string = response.body().string();
                                                    JsonNode node = mapper.readValue(string, JsonNode.class);
                                                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                                                    if (statusCode == 0) {
                                                        JsonNode responseNode = node.get("response");
                                                        UpdateResponse data = mapper.treeToValue(responseNode, UpdateResponse.class);
                                                        //更新ui
                                                        Bundle resultBundle = new Bundle();
                                                        resultBundle.putString(
                                                                SetInformationActivity.NAME_RESULT,
                                                                data.getName());
                                                        resultBundle.putString(
                                                                SetInformationActivity.IMAGE_RESULT,
                                                                data.getHeadImage());
                                                        Intent resultIntent = new Intent()
                                                                .putExtras(resultBundle);
                                                        setResult(
                                                                NAME_IMAGE_RESULT_CODE,
                                                                resultIntent);
                                                    } else {
                                                        JsonNode errorNode = node.get("error");
                                                        Error error = mapper.treeToValue(errorNode, Error.class);
                                                        showShortToast(error.getInfo());
                                                    }
                                                    mDelivery.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            showShortToast("上传成功");
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                }

                                            }
                                        }

        );
    }

    //选择性别

    private void checkSexDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_check_sex, null);
        dialog = new AlertDialog.Builder(this).create();  //先得到构造器
        dialog.show();
        /*WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = (int) Application.getInstance().getResources().getDimension(R.dimen.dialog_widths);
        dialog.getWindow().setAttributes(params);*/
        dialog.getWindow().setContentView(view);
        man = (RadioButton) view.findViewById(R.id.dialog_sex__man);
        woman = (RadioButton) view.findViewById(R.id.dialog_sex__woman);
        String sex = sexTv.getText().toString().trim();
        if ("女".equals(sex)) {
            woman.setChecked(true);
            man.setChecked(false);
        } else {
            man.setChecked(true);
            woman.setChecked(false);
        }
        man.setOnClickListener(this);
        woman.setOnClickListener(this);
    }
}

