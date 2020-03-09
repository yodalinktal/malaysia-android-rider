package com.bsmart.pos.rider.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.StringUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseActivity;
import com.bsmart.pos.rider.base.BaseQRCodeFragment;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.utils.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: yoda
 * DateTime: 2020/3/7 22:11
 */
public class ConfirmDeliveryActivity extends BaseActivity {


    @BindView(R.id.header)
    HeaderView header;

    @BindView(R.id.btnDelivery)
    Button btnDelivery;

    @BindView(R.id.orderNo)
    TextView orderNo;

    @BindView(R.id.fromInfo)
    TextView fromInfo;

    @BindView(R.id.toInfo)
    TextView toInfo;

    @BindView(R.id.tipsImageView)
    ImageView tipsImageView;

    @BindView(R.id.tipsTitle)
    TextView tipsTitle;

    @BindView(R.id.tipsSecondTitle)
    TextView tipsSecondTitle;

    @BindView(R.id.etCenterCode)
    EditText etCenterCode;

    private OrderBean orderBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);
        ButterKnife.bind(this);
        header.setTitle(getResources().getString(R.string.title_activity_Delivery));
        header.setLeft(view->finish());
        btnDelivery.setOnClickListener(onDeliveryListener);

        Intent intent = getIntent();
        String orderInfo = intent.getStringExtra(BaseQRCodeFragment.ORDERINFO);

        if (!StringUtils.isEmpty(orderInfo)){
            try {
                orderBean = App.gson.fromJson(orderInfo, OrderBean.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (null != orderBean){
            orderNo.setText("Order No:"+orderBean.getOrderNo());
            fromInfo.setText("From:"+orderBean.getFrom().getName()+", "+orderBean.getFrom().getTelephone());
            toInfo.setText("To:"+orderBean.getTo().getName()+", "+orderBean.getTo().getTelephone());

        }else{
            orderNo.setText("Data is invalid!");
            fromInfo.setText("");
            toInfo.setText("");
            tipsImageView.setImageResource(R.mipmap.icon_caution);
            tipsTitle.setText("Scan recognition error!");
            tipsSecondTitle.setText("");
            btnDelivery.setVisibility(View.INVISIBLE);
            etCenterCode.setVisibility(View.INVISIBLE);
        }


    }

    private View.OnClickListener onDeliveryListener = view -> {

        //todo:更新订单状态，然后退出；

        finish();

    };

}
