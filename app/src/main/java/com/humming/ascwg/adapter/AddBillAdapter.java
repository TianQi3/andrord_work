package com.humming.ascwg.adapter;

import com.humming.ascwg.R;
import com.wg.baseinfo.dto.SepInvoiceListResponse;

import java.util.List;


/**
 * Created by Elvira on 2016/8/5.
 */
public class AddBillAdapter extends BaseAdapter<SepInvoiceListResponse> {


    public AddBillAdapter( List<SepInvoiceListResponse> data) {
        super(R.layout.item_bill_sep, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SepInvoiceListResponse item, int position) {
      //  helper.setText(R.id.item_surprise__title, item.getName())
        //        .setText(R.id.item_surprise__content, item.getTitle());
       // Picasso.with(helper.getConvertView().getContext()).load(item.getBgImage()).into((ImageView) helper.getView(R.id.item_surprise__image));
    }

}
