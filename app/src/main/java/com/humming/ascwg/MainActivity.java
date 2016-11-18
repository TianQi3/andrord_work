package com.humming.ascwg;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.activity.AbstractActivity;
import com.humming.ascwg.activity.SetInformationActivity;
import com.humming.ascwg.adapter.ViewPagerAdapter;
import com.humming.ascwg.component.LockableViewPager;
import com.humming.ascwg.content.MyContent;
import com.humming.ascwg.content.ProductContent;
import com.humming.ascwg.content.RightsContent;
import com.humming.ascwg.content.ShoppingCartContent;
import com.humming.ascwg.content.SurpriseContent;
import com.humming.ascwg.requestUtils.RequestNull;
import com.humming.ascwg.service.DownLoadReceive;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.baseinfo.dto.MobileVersionResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractActivity {
    private ViewPagerAdapter adapter;
    private LockableViewPager viewPager;
    private int[] tabIcons = {R.drawable.icon_product, R.drawable.icon_suprise, R.drawable.icon_quanyi, R.drawable.icon_shopping, R.drawable.icon_me};
    private int[] tabSelectedIcons = {R.drawable.icon_product_select, R.drawable.icon_suprise_select, R.drawable.icon_quanyi_select, R.drawable.icon_shopping_select, R.drawable.icon_me_select};
    private final String[] titles = {Application.getInstance().getResources().getString(R.string.tab_product), Application.getInstance().getResources().getString(R.string.tab_surprise), Application.getInstance().getResources().getString(R.string.tab_rights), Application.getInstance().getResources().getString(R.string.tab_cart), Application.getInstance().getResources().getString(R.string.tab_my)};
    private LinearLayout toolbarSurprise;
    private TextView toolbarTitle;
    private TextView toolbarActivity;
    private TextView toolbarPromotion;
    private Application mAPP;
    private MyHandler mHandler;
    private ProductContent productContent;
    private SurpriseContent surpriseContent;
    private ShoppingCartContent shoppingCartContent;
    private MyContent myContent;
    private RightsContent rightsContent;
    private long mExitTime; //退出时间
    private MobileVersionResponse versionResponse;
    private DownloadManager downloadManager;
    private DownLoadReceive receiver;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAPP = (Application) getApplication();
        mHandler = new MyHandler();
        mAPP.setHandler(mHandler);
        context = this;
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSurprise = (LinearLayout) findViewById(R.id.toolbar_activity_promotion);
        toolbarActivity = (TextView) findViewById(R.id.toolbar_activity);
        toolbarPromotion = (TextView) findViewById(R.id.toolbar_promotion);
        //惊喜中  活动点击事件
        toolbarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarPromotion.setTextColor(getResources().getColor(R.color.tab_text));
                toolbarActivity.setTextColor(getResources().getColor(R.color.tab_bg));
                surpriseContent.eventOrpromotion(true);
            }
        });
        //惊喜中促销点击事件
        toolbarPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarPromotion.setTextColor(getResources().getColor(R.color.tab_bg));
                toolbarActivity.setTextColor(getResources().getColor(R.color.tab_text));
                surpriseContent.eventOrpromotion(false);
            }
        });
        //tabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.content_main_tab_layout);
        final TabLayout.Tab[] tabs = {tabLayout.newTab().setText(titles[0]).setIcon(tabIcons[0]), tabLayout.newTab().setText(titles[1]).setIcon(tabIcons[1]), tabLayout.newTab().setText(titles[2]).setIcon(tabIcons[2]), tabLayout.newTab().setText(titles[3]).setIcon(tabIcons[3]), tabLayout.newTab().setText(titles[4]).setIcon(tabIcons[4])};

        tabLayout.addTab(tabs[0]);
        tabLayout.addTab(tabs[1]);
        tabLayout.addTab(tabs[2]);
        tabLayout.addTab(tabs[3]);
        tabLayout.addTab(tabs[4]);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (LockableViewPager) findViewById(R.id.content_main_viewpager);
        initPageContent();
        List<View> list = new ArrayList<View>();
        list.add(productContent);
        list.add(surpriseContent);
        list.add(rightsContent);
        list.add(shoppingCartContent);
        list.add(myContent);
        adapter = new ViewPagerAdapter(list, titles);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        // checkUpdate();//检查更新
        TabLayout.TabLayoutOnPageChangeListener onPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            private int lastIndex = -1;

            @Override
            public void onPageSelected(int position) {
                if (lastIndex >= 0) {
                    tabs[lastIndex].setIcon(tabIcons[lastIndex]);
                }
                tabs[position].setIcon(tabSelectedIcons[position]);
                lastIndex = position;
                if (position == 0) {
                    toolbar.setVisibility(View.GONE);
                } else if (position == 1) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbarSurprise.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.GONE);
                } else if (position == 2) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbarSurprise.setVisibility(View.GONE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    toolbarTitle.setText(titles[position]);
                    rightsContent.initData(context);
                } else if (position == 3) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbarSurprise.setVisibility(View.GONE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    toolbarTitle.setText(titles[position]);
                    shoppingCartContent.initData();
                } else if (position == 4) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbarSurprise.setVisibility(View.GONE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    toolbarTitle.setText(titles[position]);
                }
                invalidateOptionsMenu();
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        onPageChangeListener.onPageSelected(0);
    }

    private void initPageContent() {
        Context context = getBaseContext();
        rightsContent = new RightsContent(context);
        productContent = new ProductContent(context);
        surpriseContent = new SurpriseContent(context);
        myContent = new MyContent(context);
        shoppingCartContent = new ShoppingCartContent(context);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, Application.getInstance().getString(R.string.application_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyHandler extends Handler {

        public String msgContent;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            shoppingCartContent.initData();
        }

    }

    //检查更新
    private void checkUpdate() {
        String tType = GetNetworkType();
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.MOBILE_VERSION, new OkHttpClientManager.ResultCallback<MobileVersionResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(MobileVersionResponse response) {
                PackageInfo pi = null;
                PackageManager pm = getPackageManager();
                try {
                    pi = pm.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (Float.parseFloat(response.getAndrVersion()) > Float.parseFloat((pi.versionName))) {
                    //提示更新
                    showUpdataDialog();
                } else {
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, MobileVersionResponse.class);
    }


    //提示更新的dailog

    private void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        // builer.setTitle(versionInfo.getAndrTitle());
        //  builer.setMessage(versionInfo.getAndrDescription().toString());
        builer.setTitle(versionResponse.getAndrTitle());
        StringBuilder sb = new StringBuilder();
        for (String msg : versionResponse.getAndrDescription()) {
            sb.append(msg + "\n");
        }
        builer.setMessage(sb.toString());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String netType = GetNetworkType();
                if ("wifi".equalsIgnoreCase(netType)) {
                    downLoadApk();
                } else {
                    dialog.dismiss();
                    showNetWorkDialog();
                }
            }
        });
        //如果是andrNeedUpd = 1; 必须更新  0 则可以选择取消
        if ("0".equals(versionResponse.getAndrNeedUpd())) {
            //当点取消按钮时进行登录
            builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
        }

        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    //提示更新的dailog
    private void showNetWorkDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle(Application.getInstance().getString(R.string.update_prompt));

        builer.setMessage(Application.getInstance().getString(R.string.net_type_message));
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });

        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showUpdataDialog();
            }
        });

        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    //下载apk
    private void downLoadApk() {
        //    final ProgressDialog pd;    //进度条对话框
        //   pd = new ProgressDialog(this);
        //   pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //   pd.setMessage("正在下载更新");
        //    pd.show();
        //下载apk
        new Thread() {
            @Override
            public void run() {
                try {
                    //  String apkUrl = versionInfo.getAndrLink();
                    String apkUrl = "http://101.231.101.70:8080/mobile-sales.apk";
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
                    request.setDestinationInExternalPublicDir("download", "salesMobile.apk");
                    request.setDescription("salesMobile新版本下载");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setMimeType("application/vnd.android.package-archive");
                    // 设置为可被媒体扫描器找到
                    request.allowScanningByMediaScanner();
                    // 设置为可见和可管理
                    request.setVisibleInDownloadsUi(true);
                    long refernece = downloadManager.enqueue(request);
                    SharedPreferences spf = MainActivity.this.getSharedPreferences("download", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putLong("download_id", refernece);//保存下载ID
                    editor.commit();
                    receiver = new DownLoadReceive();
                    MainActivity.this.registerReceiver(receiver, new IntentFilter(
                            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    //  installApk(refernece);
                    //    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public String GetNetworkType() {
        String strNetworkType = "";

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }
        return strNetworkType;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        switch (requestCode) {
            //更新个人信息
            case SetInformationActivity.NAME_IMAGE_RESULT_CODE:
                String userName = resultBundle.getString(SetInformationActivity.NAME_RESULT);
                String imageUrl = resultBundle.getString(SetInformationActivity.IMAGE_RESULT);
                myContent.refreshUI(userName, imageUrl);
        }
    }
}


//