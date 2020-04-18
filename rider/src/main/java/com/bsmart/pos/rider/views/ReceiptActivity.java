package com.bsmart.pos.rider.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseActivity;
import com.bsmart.pos.rider.base.BaseQRCodeFragment;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.api.enums.PayConstant;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.tools.DateUtil;
import com.bsmart.pos.rider.tools.OrderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 骑手预览收据，建议是截图发给用户
 * Author: yoda
 * DateTime: 2020/4/18 22:23
 */
public class ReceiptActivity extends BaseActivity {

    @BindView(R.id.header)
    HeaderView header;

    @BindView(R.id.saleType)
    TextView saleType;

    @BindView(R.id.receiptNo)
    TextView receiptNo;

    @BindView(R.id.itemWeight)
    TextView itemWeight;

    @BindView(R.id.itemCount)
    TextView itemCount;

    @BindView(R.id.itemPrice)
    TextView itemPrice;

    @BindView(R.id.orderNo)
    TextView orderNo;

    @BindView(R.id.createTime)
    TextView createTime;

    private OrderBean orderBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        ButterKnife.bind(this);
        header.setLeft(view -> finish());
        header.setTitle(getResources().getString(R.string.title_activity_Receipt));

        Intent intent = getIntent();
        String orderInfo = intent.getStringExtra(BaseQRCodeFragment.ORDERINFO);

        if (!StringUtils.isEmpty(orderInfo)){
            try {
                orderBean = App.gson.fromJson(orderInfo, OrderBean.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (null!=orderBean){
            saleType.setText("Sale Type: "+ PayConstant.getInstance().TYPE_ENUM.get(orderBean.getPayType()));
            receiptNo.setText("receiptNo:"+ OrderUtil.formatReceiptNo(orderBean.get_id()));
            itemWeight.setText("Parcel( "+orderBean.getSizeWeight()+"kg )");
            itemPrice.setText(""+new Double(orderBean.getAmount())/100.00);
            itemCount.setText(""+1);
            orderNo.setText("Tracking Num: "+ OrderUtil.formatOrderNo(orderBean.getOrderNo()));
            createTime.setText("Date: "+ DateUtil.formatTimestampEnglish(orderBean.getCreatedTimestamp()));
        }
    }

}
