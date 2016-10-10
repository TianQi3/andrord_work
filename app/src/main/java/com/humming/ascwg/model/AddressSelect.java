package com.humming.ascwg.model;

/**
 * Created by Zhtq on 16/8/15.
 */
public class AddressSelect {
    private String name;
    private boolean select;

    public AddressSelect() {
    }

    public AddressSelect(String name, boolean select) {
        this.name = name;
        this.select = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
