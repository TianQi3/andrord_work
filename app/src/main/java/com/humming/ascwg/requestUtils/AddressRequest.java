package com.humming.ascwg.requestUtils;

import com.humming.ascwg.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/16.
 */
public class AddressRequest implements IRequestMainData {
    private long shippingAddressId;
    private String contact;
    private String phone;
    private String provinceName;
    private String cityName;
    private String countyName;
    private String detailAddress;
    private int defaultFlg;

    public AddressRequest() {
    }

    public AddressRequest(String contact, long shippingAddressId, String phone, String provinceName, String cityName, String countyName, String detailAddress, int defaultFlg) {
        this.contact = contact;
        this.shippingAddressId = shippingAddressId;
        this.phone = phone;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.countyName = countyName;
        this.detailAddress = detailAddress;
        this.defaultFlg = defaultFlg;
    }

    public long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getDefaultFlg() {
        return defaultFlg;
    }

    public void setDefaultFlg(int defaultFlg) {
        this.defaultFlg = defaultFlg;
    }
}
