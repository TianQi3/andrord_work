package com.humming.ascwg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.my.AddressMessageActivity;
import com.humming.ascwg.activity.my.MyAddressActivity;
import com.humming.ascwg.model.AddressSelect;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.AddressRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.wg.order.dto.ShippingAddressListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class MyAddressAdapter extends AbstractItemPagerArrayAdapter<ShippingAddressListResponse, ViewHolders> {
    private View.OnClickListener onbtnDeleteClickListener;
    private View.OnClickListener onbtnEditClickListener;
    private View.OnClickListener onbtnItemClickListener;
    private Context context;
    private List<ShippingAddressListResponse> shippingAddressListResponses;
    private List<AddressSelect> addressSelectList = new ArrayList<AddressSelect>();

    public MyAddressAdapter(Context context, int resource, final List<ShippingAddressListResponse> items, int[] itemPageResArray, int defaultPageIndex, String types) {
        super(context, resource, items, itemPageResArray, defaultPageIndex, types);
        this.context = context;
        this.shippingAddressListResponses = items;
        for (ShippingAddressListResponse shippingAddressListResponse : items) {
            AddressSelect addressSelect = new AddressSelect();
            if (shippingAddressListResponse.getDefaultFlg() == 1) {
                addressSelect.setSelect(true);
            } else {
                addressSelect.setSelect(false);
            }
            addressSelectList.add(addressSelect);
        }
        onbtnDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除address
                final int position = Integer.parseInt(v.getTag().toString());
                AddressRequest addressRequest = new AddressRequest();
                addressRequest.setShippingAddressId(items.get(position).getShippingAddressId());
                OkHttpClientManager.postAsyn(Config.ADDRESS_DELETE, new OkHttpClientManager.ResultCallback<ResponseData>() {
                    @Override
                    public void onError(Request request, Error info) {
                        Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                        Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResponseData response) {
                        items.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(Application.getInstance().getCurrentActivity(), Application.getInstance().getResources().getString(R.string.delete_address_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onOtherError(Request request, Exception exception) {
                        Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                    }
                }, addressRequest, ResponseData.class);
                updateViewPagerState(position, 0, true);
            }
        };
        onbtnItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = Integer.parseInt(v.getTag().toString());
                if (MyAddressActivity.addressType != null && "1".equals(MyAddressActivity.addressType)) {

                } else {
                    Bundle resultBundles = new Bundle();
                    resultBundles.putString(
                            MyAddressActivity.GET_ADDRESS, items.get(position).getCountyName() + items.get(position).getCityName() +
                                    items.get(position).getDetailAddress());
                    resultBundles.putString(
                            MyAddressActivity.GET_NAME,
                            items.get(position).getContact());
                    resultBundles.putString(
                            MyAddressActivity.GET_PHONE,
                            items.get(position).getPhone());
                    resultBundles.putString(
                            MyAddressActivity.GET_SHIPPINGADDRESSID,
                            items.get(position).getShippingAddressId() + "");

                    Intent resultIntent = new Intent()
                            .putExtras(resultBundles);
                    Application.getInstance().getCurrentActivity().setResult(MyAddressActivity.ADDRESS_CODE,
                            resultIntent);
                    Application.getInstance().getCurrentActivity().finish();
                }
            }
        };
        //编辑
        onbtnEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positions = Integer.parseInt(v.getTag().toString());
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), AddressMessageActivity.class);
                intent.putExtra(AddressMessageActivity.UPDATE_OR_ADD, "0");
                intent.putExtra(AddressMessageActivity.CONSIGNEE, items.get(positions).getContact());
                intent.putExtra(AddressMessageActivity.ADDRESS, items.get(positions).getDetailAddress());
                intent.putExtra(AddressMessageActivity.CONTACT, items.get(positions).getPhone());
                intent.putExtra(AddressMessageActivity.SHIPPING_ID, items.get(positions).getShippingAddressId() + "");
                intent.putExtra(AddressMessageActivity.PROVINCE_CITY, items.get(positions).getCountyName() + items.get(positions).getCityName() + "");
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, AddressMessageActivity.ACTIVITY_ADDRESS_RESULT);
            }
        };
    }

    @Override
    protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
        ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
            @Override
            public float getPageWidth(int position) {
                if (position == 1) {
                    return 0.20f;
                } else {
                    return 1f;
                }
            }
        };
        return adapter;
    }

    @Override
    protected ViewHolders createViewHolder() {
        return new ViewHolders();
    }

    @Override
    protected void initViewHolder(final int position, final ViewHolders viewHolder, List<View> itemPages) {
        View rightView = itemPages.get(1);
        View centerView = itemPages.get(0);
        viewHolder.v = centerView;
        viewHolder.mEdit = (TextView) centerView.findViewById(R.id.list_item_address__edit);
        viewHolder.mDelete = (TextView) centerView.findViewById(R.id.list_item_address__delete);
        viewHolder.mName = (TextView) centerView.findViewById(R.id.list_item_address__name);
        viewHolder.mPhone = (TextView) centerView.findViewById(R.id.list_item_address__phone);
        viewHolder.mAddress = (TextView) centerView.findViewById(R.id.list_item_address__address);
        viewHolder.setDefault = (CheckBox) centerView.findViewById(R.id.list_item_address__defult);
        viewHolder.defaultLayout = (LinearLayout) centerView.findViewById(R.id.list_item_address__defult_layout);
        viewHolder.mDelete.setOnClickListener(onbtnDeleteClickListener);
        viewHolder.btnDelete = (Button) rightView
                .findViewById(R.id.item_delete);
        viewHolder.btnDelete.setVisibility(View.VISIBLE);
        viewHolder.btnDelete.setText(Application.getInstance().getString(R.string.delete));
        viewHolder.btnDelete.setOnClickListener(onbtnDeleteClickListener);
        viewHolder.mEdit.setOnClickListener(onbtnEditClickListener);
        viewHolder.v.setOnClickListener(onbtnItemClickListener);
        viewHolder.v.setTag(position);
        viewHolder.mEdit.setTag(position);
        viewHolder.btnDelete.setTag(position);
        viewHolder.mDelete.setTag(position);

    }

    @Override
    protected void setItemData(final int position, final ViewHolders viewHolder, ShippingAddressListResponse itemData) {
        viewHolder.mAddress.setText(itemData.getCountyName() + itemData.getCityName() + itemData.getDetailAddress());
        viewHolder.mPhone.setText(itemData.getPhone());
        viewHolder.mName.setText(itemData.getContact());
        if (addressSelectList.get(position).isSelect()) {
            viewHolder.setDefault.setChecked(true);
        } else {
            viewHolder.setDefault.setChecked(false);
        }
        viewHolder.defaultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.setDefault.isChecked()) {
                    viewHolder.setDefault.setChecked(false);
                } else {
                    viewHolder.setDefault.setChecked(true);
                }
            }
        });
        viewHolder.setDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AddressRequest addressRequest = new AddressRequest();
                    addressRequest.setShippingAddressId(shippingAddressListResponses.get(position).getShippingAddressId());
                    OkHttpClientManager.postAsyn(Config.SET_DEFAULT, new OkHttpClientManager.ResultCallback<ResponseData>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void onResponse(ResponseData response) {
                            for (AddressSelect addressSelect : addressSelectList) {
                                addressSelect.setSelect(false);
                            }
                            addressSelectList.get(position).setSelect(true);
                            notifyDataSetChanged();
                            Toast.makeText(Application.getInstance().getCurrentActivity(), Application.getInstance().getResources().getString(R.string.set_default_success), Toast.LENGTH_SHORT).show();
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

}

class ViewHolders {
    public TextView mEdit;
    public TextView mDelete;
    public TextView mName;
    public TextView mPhone;
    public TextView mAddress;
    public Button btnDelete;
    public CheckBox setDefault;
    public LinearLayout defaultLayout;
    View v;
}

