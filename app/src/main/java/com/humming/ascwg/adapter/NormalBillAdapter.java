package com.humming.ascwg.adapter;

import com.humming.ascwg.R;
import com.wg.baseinfo.dto.NorInvoiceListResponse;

import java.util.List;

/**
 * Created by Elvira on 2016/8/5.
 */
public class NormalBillAdapter extends BaseAdapter<NorInvoiceListResponse> {

    public NormalBillAdapter(List<NorInvoiceListResponse> list) {
        super(R.layout.list_item_nor_bill, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, NorInvoiceListResponse item, int position) {
        helper.setText(R.id.item_bill__address, item.getContactAddress())
                .setText(R.id.item_bill__user, item.getContact())
                .setText(R.id.item_bill__phone, item.getContactPhone())
                .setText(R.id.item_bill__company_heard, item.getCompanyName());
        // Picasso.with(helper.getConvertView().getContext()).load(item.getBgImage()).into((ImageView) helper.getView(R.id.item_surprise__image));
    }

}
