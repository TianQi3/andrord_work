package com.humming.ascwg;

import android.app.Activity;

import com.humming.ascwg.model.OrderSelect;
import com.humming.ascwg.model.TextEditorData;
import com.wg.order.dto.OrderDetailResponse;
import com.wg.promotion.dto.GifResponse;
import com.wg.promotion.dto.PackageRespones;
import com.wg.promotion.dto.SalesRespones;
import com.wg.promotion.dto.SelectPackageRespones;

import java.util.List;
import java.util.Stack;

/**
 * Created by Ztq on 16/8/3.
 */
public class Application extends android.app.Application {
    //  private String dataUrl = "http://139.224.21.29/cgi";
    public static final String HEADER_TOKEN = "Token";
    private Activity currentActivity;
    private TextEditorData textEditorData;
    private MainActivity.MyHandler handler = null;
    private static Stack<Activity> activityStack;


    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";


    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static Application sInstance;
    private List<OrderSelect> orderSelectList;
    private List<OrderSelect> rightSelectList;
    private OrderDetailResponse orderDetailResponse;
    private GifResponse gifResponse;
    private PackageRespones packageRespones;
    private SelectPackageRespones selectPackageRespones;
    private SalesRespones salesRespones;

    public PackageRespones getPackageRespones() {
        return packageRespones;
    }

    public void setPackageRespones(PackageRespones packageRespones) {
        this.packageRespones = packageRespones;
    }

    public SelectPackageRespones getSelectPackageRespones() {
        return selectPackageRespones;
    }

    public void setSelectPackageRespones(SelectPackageRespones selectPackageRespones) {
        this.selectPackageRespones = selectPackageRespones;
    }

    public SalesRespones getSalesRespones() {
        return salesRespones;
    }

    public void setSalesRespones(SalesRespones salesRespones) {
        this.salesRespones = salesRespones;
    }

    public GifResponse getGifResponse() {
        return gifResponse;
    }

    public void setGifResponse(GifResponse gifResponse) {
        this.gifResponse = gifResponse;
    }

    public OrderDetailResponse getOrderDetailResponse() {
        return orderDetailResponse;
    }

    public void setOrderDetailResponse(OrderDetailResponse orderDetailResponse) {
        this.orderDetailResponse = orderDetailResponse;
    }

    public List<OrderSelect> getOrderSelectList() {
        return orderSelectList;
    }

    public void setOrderSelectList(List<OrderSelect> orderSelectList) {
        this.orderSelectList = orderSelectList;
    }

    public List<OrderSelect> getRightSelectList() {
        return rightSelectList;
    }

    public void setRightSelectList(List<OrderSelect> rightSelectList) {
        this.rightSelectList = rightSelectList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the singleton
        sInstance = this;
    }

    /**
     * @return Application singleton instance
     */
    public static synchronized Application getInstance() {
        return sInstance;

    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public TextEditorData getTextEditorData() {
        return textEditorData;
    }

    public void setTextEditorData(TextEditorData textEditorData) {
        this.textEditorData = textEditorData;
    }

    // set方法
    public void setHandler(MainActivity.MyHandler handler) {
        this.handler = handler;
    }

    // get方法
    public MainActivity.MyHandler getHandler() {
        return handler;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
}

