package com.humming.ascwg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.ascwg.Application;
import com.humming.ascwg.Config;
import com.humming.ascwg.Constant;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.product.WinesInfoActivity;
import com.humming.ascwg.content.ShoppingCartContent;
import com.humming.ascwg.model.OrderSelect;
import com.humming.ascwg.model.ResponseData;
import com.humming.ascwg.requestUtils.ShoppingCartRequest;
import com.humming.ascwg.service.Error;
import com.humming.ascwg.service.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ztq on 16/8/4.
 */
public class CartRecycleAdapter extends AbstractItemPagerArrayAdapter<OrderSelect, ViewHolder> {
    private View.OnClickListener onbtnDeleteClickListener;
    private View.OnClickListener onbtnItemClickListener;
    private String types;
    private Context context;
    public List<OrderSelect> orderSelects;

    public CartRecycleAdapter(final Context context, int resource, final List<OrderSelect> items, int[] itemPageResArray, int defaultPageIndex, String types) {
        super(context, resource, items, itemPageResArray, defaultPageIndex, types);
        this.types = types;
        this.context = context;
        this.orderSelects = items;

        onbtnDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除product
                final int position = Integer.parseInt(v.getTag().toString());
                ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest();
                shoppingCartRequest.setShoppingCartId(items.get(position).getShoppingCartId());
                OkHttpClientManager.postAsyn(Config.SHOPPINGCART_DELETE, new OkHttpClientManager.ResultCallback<ResponseData>() {
                    @Override
                    public void onError(Request request, Error info) {
                        Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                        Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResponseData response) {
                        items.remove(position);
                        notifyDataSetChanged();
                        if (items.size() == 0) {
                            ShoppingCartContent.normalAll.setVisibility(View.GONE);
                        }
                        ShoppingCartContent.measureListViewHeight(ShoppingCartContent.mRecycleView);
                        Toast.makeText(Application.getInstance().getCurrentActivity(), Application.getInstance().getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onOtherError(Request request, Exception exception) {
                        Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                    }
                }, shoppingCartRequest, ResponseData.class);
                updateViewPagerState(position, 0, true);
            }
        };
        onbtnItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positions = Integer.parseInt(v.getTag().toString());
                Intent intent = new Intent(context, WinesInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.ID, items.get(positions).getItemId());
                intent.putExtras(bundle);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                updateViewPagerState(positions, 0, true);
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
    protected ViewHolder createViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void initViewHolder(int position, ViewHolder viewHolder, List<View> itemPages) {
        View rightView = itemPages.get(1);
        View centerView = itemPages.get(0);
        viewHolder.v = centerView;
        viewHolder.mCartRadioLayout = (LinearLayout) centerView.findViewById(R.id.item_shoppongcart__radiogroup);
        viewHolder.mCartLayout = (LinearLayout) centerView.findViewById(R.id.item_shoppongcart__layouts);
        viewHolder.mCartItemRadio = (CheckBox) centerView.findViewById(R.id.item_shoppongcart__radio);
        viewHolder.mCartImg = (ImageView) centerView.findViewById(R.id.item_shoppongcart__img);
        viewHolder.mCartName = (TextView) centerView.findViewById(R.id.item_shoppongcart__name);
        viewHolder.mCartEngName = (TextView) centerView.findViewById(R.id.item_shoppongcart__englishname);
        viewHolder.mCartPrice = (TextView) centerView.findViewById(R.id.item_shoppongcart__price);
        viewHolder.mCartNumber = (TextView) centerView.findViewById(R.id.item_shoppongcart__number);
        viewHolder.mCartsub = (LinearLayout) centerView.findViewById(R.id.item_shoppongcart__subtract);
        viewHolder.mCartAdd = (LinearLayout) centerView.findViewById(R.id.item_shoppongcart__add);

        viewHolder.btnDelete = (Button) rightView
                .findViewById(R.id.item_delete);
        viewHolder.btnDelete.setVisibility(View.VISIBLE);
        viewHolder.btnDelete.setText(Application.getInstance().getString(R.string.delete));
        viewHolder.btnDelete.setOnClickListener(onbtnDeleteClickListener);
        viewHolder.v.setOnClickListener(onbtnItemClickListener);
    }

    @Override
    protected void setItemData(final int position, final ViewHolder viewHolder, final OrderSelect itemData) {
        if (orderSelects.get(position).isSelect()) {
            viewHolder.mCartItemRadio.setChecked(true);
        } else {
            viewHolder.mCartItemRadio.setChecked(false);
        }
        viewHolder.btnDelete.setTag(position);
        viewHolder.v.setTag(position);
        viewHolder.mCartName.setText(itemData.getNameCn());
        viewHolder.mCartEngName.setText(itemData.getNameEn());
        viewHolder.mCartPrice.setText(itemData.getCostPrice() + "");
        viewHolder.mCartNumber.setText(itemData.getQuantity() + "");
        Picasso.with(context).load(itemData.getImageUrl()).error(R.drawable.shopping_cart_item).into(viewHolder.mCartImg);
        //减数量
        viewHolder.mCartsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(viewHolder.mCartNumber.getText().toString());
                if (number == 1) {
                    //  viewHolder.mCartNumber.setTextColor(Application.getInstance().getResources().getColor(R.color.text_gray));
                } else {
                    number--;
                    orderSelects.get(position).setQuantity(orderSelects.get(position).getQuantity() - 1);
                    viewHolder.mCartNumber.setText(number + "");
                    if (viewHolder.mCartItemRadio.isChecked() && orderSelects.get(position).isSelect()) {
                        ShoppingCartContent.totalMoney((int) itemData.getCostPrice(), 0);
                        ShoppingCartContent.totalNumber(1, 0);
                    } else {
                    }

                }
            }
        });
        //加数量
        viewHolder.mCartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(viewHolder.mCartNumber.getText().toString());
                number++;
                orderSelects.get(position).setQuantity(orderSelects.get(position).getQuantity() + 1);
                viewHolder.mCartNumber.setText(number + "");
                if (viewHolder.mCartItemRadio.isChecked() && orderSelects.get(position).isSelect()) {
                    ShoppingCartContent.totalMoney((int) itemData.getCostPrice(), 1);
                    ShoppingCartContent.totalNumber(1, 1);
                } else {
                }
            }
        });
        //是否选中
        viewHolder.mCartRadioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.mCartItemRadio.isChecked()) {
                    viewHolder.mCartItemRadio.setChecked(false);
                } else {
                    viewHolder.mCartItemRadio.setChecked(true);
                }
            }
        });

        viewHolder.mCartItemRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int itemTotal = 0;
                int number = (Integer.parseInt(viewHolder.mCartNumber.getText().toString()));
                itemTotal = (int) ((Integer.parseInt(viewHolder.mCartNumber.getText().toString())) * itemData.getCostPrice());
                if (isChecked) {
                    orderSelects.get(position).setSelect(true);
                    ShoppingCartContent.orderSelectArrayList.get(position).setSelect(true);
                    boolean b = false;
                    for (OrderSelect o : orderSelects) {
                        if (!o.isSelect()) {
                            b = true;
                        }
                    }
                    if (!b) {
                        ShoppingCartContent.someOrAllSelect = false;
                        ShoppingCartContent.selectNormal.setChecked(true);
                        if (ShoppingCartContent.selectYum.isChecked()) {
                            ShoppingCartContent.AllSelect = false;
                            ShoppingCartContent.selectAll.setChecked(true);
                        }
                    }
                    ShoppingCartContent.totalMoney(itemTotal, 1);
                    ShoppingCartContent.totalNumber(number, 1);
                } else {
                    ShoppingCartContent.orderSelectArrayList.get(position).setSelect(false);
                    orderSelects.get(position).setSelect(false);
                    ShoppingCartContent.totalMoney(itemTotal, 0);
                    ShoppingCartContent.totalNumber(number, 0);
                    if (ShoppingCartContent.selectNormal.isChecked()) {
                        ShoppingCartContent.someOrAllSelect = false;
                        ShoppingCartContent.selectNormal.setChecked(false);
                    }
                    if (ShoppingCartContent.selectAll.isChecked()) {
                        ShoppingCartContent.AllSelect = false;
                        ShoppingCartContent.selectAll.setChecked(false);
                    }
                }
            }
        });
    }

}

class ViewHolder {
    public LinearLayout mCartLayout;
    public CheckBox mCartItemRadio;
    public ImageView mCartImg;
    public TextView mCartName;
    public TextView mCartEngName;
    public TextView mCartPrice;
    public TextView mCartNumber;
    public LinearLayout mCartsub;
    public LinearLayout mCartAdd;
    public Button btnDelete;
    public LinearLayout mCartRadioLayout;
    View v;
}