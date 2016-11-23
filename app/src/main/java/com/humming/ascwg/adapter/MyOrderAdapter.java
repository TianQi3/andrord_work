package com.humming.ascwg.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.ascwg.R;
import com.squareup.picasso.Picasso;
import com.wg.order.dto.OrderHead;
import com.wg.order.dto.OrderItemDetail;
import com.wg.order.dto.OrderListResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/8.
 */
public class MyOrderAdapter extends BaseAdapter<OrderListResponse> {

    private View view;
    private View lineView;
    private List<OrderItemDetail> orderItemDetails;

    public MyOrderAdapter(List<OrderListResponse> list) {
        super(R.layout.item_my_order, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderListResponse list, int position) {
        OrderHead orderHead = list.getOrderHeadInfo();
        // helper.setIsRecyclable(false);
        orderItemDetails = list.getItemDetailInfo();

        if (orderHead.getOrderStatus() == 1) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_1));
        } else if (orderHead.getOrderStatus() == 2) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_2));
        } else if (orderHead.getOrderStatus() == 3) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_3));
        } else if (orderHead.getOrderStatus() == 4) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_4));
        } else if (orderHead.getOrderStatus() == 5) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_5));
        } else if (orderHead.getOrderStatus() == 6) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_6));
        } else if (orderHead.getOrderStatus() == 7) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_7));
        } else if (orderHead.getOrderStatus() == 8) {
            helper.setText(R.id.item_my_order__status, helper.getView(R.id.item_my_order__status).getContext().getResources().getString(R.string.order_status_8));
        }
        helper.setFlags(R.id.item_my_order__original_price, Paint.STRIKE_THRU_TEXT_FLAG);
        int size = orderItemDetails.size();
        helper.setText(R.id.item_my_order__date, orderHead.getOrderTime()).setText(R.id.item_my_order__original_price
                , orderHead.getOrderCostTotalPrice() + "").setText(R.id.item_my_order__vip_discount, "×" + orderHead.getDiscount() + "%")
                .setText(R.id.item_my_order__vip_price, orderHead.getOrderSoldTotalPrice() + "");
        LinearLayout linearLayout = helper.getView(R.id.item_my_order__layout);
        linearLayout.removeAllViews();

        for (int i = 0; i < size; i++) {
            initView(linearLayout.getContext(), orderItemDetails.get(i));
            linearLayout.addView(view);
            if (size > 1 && i < size - 1) {
                linearLayout.addView(lineView);
            }
        }
    }

    private void initView(Context context, OrderItemDetail orderItemDetail) {
        view = LayoutInflater.from(context).inflate(R.layout.item_my_order_, null, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.item_my_order__layout_);
        ImageView wineImage = (ImageView) view.findViewById(R.id.item_my_order__wine_image);
        TextView wineName = (TextView) view.findViewById(R.id.item_my_order__wine_name);
        TextView winePrice = (TextView) view.findViewById(R.id.item_my_order__wine_price);
        TextView deletePrice = (TextView) view.findViewById(R.id.item_my_order__wine_delete_price);
        TextView wineNum = (TextView) view.findViewById(R.id.item_my_order__wine_num);
        deletePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        wineName.setText(orderItemDetail.getItemNameCn() + "   " + orderItemDetail.getItemNameEn());
        Picasso.with(context).load(orderItemDetail.getItemImage()).into(wineImage);
        winePrice.setText("¥" + orderItemDetail.getSoldUnitPrice() + "");
        deletePrice.setText("¥" + orderItemDetail.getCostUnitPrice() + "");
        wineNum.setText("×" + orderItemDetail.getQuantity() + "");

        lineView = new View(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lineView.setLayoutParams(lp);
        lineView.setBackgroundColor(ContextCompat.getColor(context, R.color.garys));
    }

}
