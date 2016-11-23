package com.humming.ascwg;

/**
 * Created by ztq on 08/05/16.
 */
public class Config {
    public static final boolean DEBUG = false;
    //public static final String URL_SERVICE = "http://192.168.1.58:8080/cgi";//测试环境
    public static final String URL_SERVICE = "http://uat.dsapp.asc-wines.com/cgi";//测试环境
    //public static final String URL_SERVICE = "http://dsapp.asc-wines.com/cgi";//外网环境

    //产品
    public static final String PRODUCT_ALL_ITEM_CODE = "item/queryAllItemCode";//获取产品全部 itemCode
    public static final String PRODUCT_ALL_COUNTRY = "baseinfo/country";//获取国家
    public static final String PRODUCT_ALL_BRAND = "baseinfo/brand";//获取brand
    public static final String PRODUCT_ALL_TYPE = "baseinfo/itemType";//获取Type
    public static final String PRODUCT_WINES_QUERY = "item/query";//获取酒列表
    public static final String GET_REGISTER_CODE = "user/register/getVerificationCode";//获取注册验证码
    public static final String GET_BACK_PWD_CODE = "user/forgetPwd";//找回密码的验证码
    public static final String CHECK_VERIFICATION_CODE = "user/register/checkVerificationCode";//校验验证码
    public static final String SET_PASSWORD = "user/register/setPwd";//设置密码
    public static final String GET_PROVINCE = "baseinfo/province";//获取省份
    public static final String GET_CITY = "baseinfo/cityOrCounty";//获取城市
    public static final String USER_INFORMATION = "user/update";//上传个人信息
    public static final String LOGIN = "user/login";//登录
    public static final String WXLOGIN = "user/wxLogin";//微信登录
    public static final String SHOPPINGCART_QUERY = "shoppingcart/query";//查询购物车
    public static final String SHOPPINGCART_QUERY_NEW = "shoppingcart/queryNew";//查询新的购物车
    public static final String SHOPPINGCART_ADD_UPDATE = "shoppingcart/save";//购物车信息添加或修改
    public static final String SHOPPINGCART_DELETE = "shoppingcart/delete";//删除购物车信息
    public static final String ITEM_QUERY_DETIAL = "item/detail";//酒的详情
    public static final String ADDRESS_QUERY = "shippingaddress/query";//收货地址查询
    public static final String ADDRESS_SAVE_UPDATE = "shippingaddress/save";//收货地址查询
    public static final String ADDRESS_DELETE = "shippingaddress/delete";//收货地址查询
    public static final String SET_DEFAULT = "shippingaddress/setDefault";//设为默认地址
    public static final String ADDRESS_DEFALULT = "shippingaddress/queryDefault";//收货默认收货地址
    public static final String INSERT_ORDER = "order/insert";//提交订单
    public static final String QUERY_ORDER = "order/query";//订单查询
    public static final String ORDER_DETAIL = "order/detail";//订单详情
    public static final String ORDER_STATUS = "order/queryOrderStatus";//订单状态
    public static final String EVENT_QUERY = "event/query";//活动查询
    public static final String PROMOTION_QUERY = "promotion/query";//促销查询
    public static final String EVENT_ATTEND = "event/attend";//参加活动
    public static final String PROMOTION_ATTEND = "promotion/attend";//参加促销
    public static final String MY_EVENT = "event/mine";//我的活动
    public static final String MY_PROMOTION = "promotion/mine";//我的促销
    public static final String CHECK_VIP_CODE = "user/checkVipCode";//vip码验证
    public static final String CHECK_YUM_CODE = "user/checkYumCode";//百盛验证
    public static final String GET_YUM_LIST = "item/queryYumItem";//百盛列表
    public static final String QUERY_RIGHTS = "rights/query";//权益查询
    public static final String SALES_QUERY = "sales/query";//销售顾问
    public static final String MOBILE_VERSION = "baseinfo/mobileVersion";//版本更新
    public static final String URL_SERVICE_ALIIMAGE = "user/update";//头像上传
    public static final String PROMOTION_DETAIL = "promotion/detail";//促销详情
    public static final String USER_SELFINFO = "user/selfInfo";//获取用户信息
    public static final String REFUND_ORDER = "order/refund";//退款申请
    public static final String CANCEL_ORDER = "order/cancel";//取消订单
    public static final String PAY_CONTENT = "pay/getContent";//获取支付签名
    public static final String QUERY_NOR = "invoice/queryNor";//查询普通发票
    public static final String QUERY_SEP = "invoice/querySep";//查询增值税发票
}
