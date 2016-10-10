package com.humming.ascwg.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Elvira on 2016/5/18.
 * 获取屏幕分辨率
 */
public class BaseTools {
    public static int getWindowWidth(Context context) {
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        return mScreenWidth;
    }

    public static int getWindowHeigh(Context context) {
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh;
    }

    /**
     * 手机号格式验证.
     * 移动:134、135、136、137、138、139、150、151、152、157、158、159、182、183、184、187、188、178(4G)、147(上网卡)
     * 联通:130、131、132、155、156、185、186、176(4G)、145(上网卡)
     * 电信:133、153、180、181、189、177(4G) 卫星通信:1349 虚拟运营商:170
     * 总结起来就是第一位必定为1，第二位必定为3或4或5或7或8，其他位置的可以为0-9
     *
     * @param str 指定的手机号码字符串
     * @return
     */
    public static Boolean isMobileNo(String str) {
        String telRegex = "1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}";
        if (isEmpty(str))
            return false;
        else
            return str.matches(telRegex);
    }

    /**
     * 是否为null或空值
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


}
