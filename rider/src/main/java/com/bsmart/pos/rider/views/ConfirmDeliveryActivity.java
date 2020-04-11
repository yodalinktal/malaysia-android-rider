package com.bsmart.pos.rider.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.api.enums.PayConstant;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.tools.OrderUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

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

    @BindView(R.id.payInfo)
    TextView payInfo;

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

    private OrderBean orderBean;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        App.addActivity(this);
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

            //check order status
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Checking... Please wait.");
            progressDialog.show();
            Map<String,Object> requestData = new HashMap<>();
            requestData.put("orderNo", orderBean.getOrderNo());

            Api.getRectsEA().orderStatus(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                                progressDialog.dismiss();
                                if (null != bean){

                                    Log.d("orderStatus",bean.toString());

                                    if (bean.get("errno").getAsInt()==0){

                                        if(bean.get("data").getAsBoolean()){
                                            orderNo.setText("Tracking Num: "+ OrderUtil.formatOrderNo(orderBean.getOrderNo()));
                                            payInfo.setText("Pay Type: "+ PayConstant.getInstance().TYPE_ENUM.get(orderBean.getPayType())+", Amount: RM "+(new Double(orderBean.getAmount())/100.00));
                                            fromInfo.setText("From: "+orderBean.getFrom().getName()+", "+orderBean.getFrom().getTelephone());
                                            toInfo.setText("To: "+orderBean.getTo().getName()+", "+orderBean.getTo().getTelephone());
                                        }else{
                                            orderNo.setText("QR Code Invalid");
                                            fromInfo.setText("");
                                            toInfo.setText("");
                                            tipsImageView.setImageResource(R.mipmap.icon_caution);
                                            tipsTitle.setText("");
                                            tipsSecondTitle.setText("");
                                            btnDelivery.setVisibility(View.INVISIBLE);
                                        }

                                    }else{
                                        Log.e("HomeFragment",bean.get("errmsg").getAsString());
                                        orderNo.setText(bean.get("errmsg").getAsString());
                                        fromInfo.setText("");
                                        toInfo.setText("");
                                        tipsImageView.setImageResource(R.mipmap.icon_caution);
                                        tipsTitle.setText("");
                                        tipsSecondTitle.setText("");
                                        btnDelivery.setVisibility(View.INVISIBLE);
                                    }

                                }else{
                                    Log.e("HomeFragment","Some error happened, Please try again later.");
                                    orderNo.setText("Some error happened, Please try again later.");
                                    fromInfo.setText("");
                                    toInfo.setText("");
                                    tipsImageView.setImageResource(R.mipmap.icon_caution);
                                    tipsTitle.setText("");
                                    tipsSecondTitle.setText("");
                                    btnDelivery.setVisibility(View.INVISIBLE);
                                }

                            }, e -> {
                                progressDialog.dismiss();
                                orderNo.setText("Some error happened, Please try again later.");
                                fromInfo.setText("");
                                toInfo.setText("");
                                tipsImageView.setImageResource(R.mipmap.icon_caution);
                                tipsTitle.setText("");
                                tipsSecondTitle.setText("");
                                btnDelivery.setVisibility(View.INVISIBLE);
                            }
                            )
                    );

        }else{
            orderNo.setText("Data is invalid,Please Retry Scan QR");
            fromInfo.setText("");
            toInfo.setText("");
            tipsImageView.setImageResource(R.mipmap.icon_caution);
            tipsTitle.setText("Scan recognition error!");
            tipsSecondTitle.setText("");
            btnDelivery.setVisibility(View.INVISIBLE);
        }


    }

    private View.OnClickListener onDeliveryListener = view -> {

        //todo:更新订单状态，然后退出；

        progressDialog.setMessage("Processing... Please wait.");
        progressDialog.show();
            Map<String,Object> requestData = new HashMap<>();
            requestData.put("token", ProfileUtils.getToken());
            requestData.put("orderNo", orderBean.getOrderNo());
            //附近的订单（状态为waiting)
            Api.getRectsEA().orderDelivery(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                        progressDialog.dismiss();
                                if (null != bean){

                                    Log.d("orderDelivery",bean.toString());

                                    if (bean.get("errno").getAsInt()==0){

                                        finish();

                                    }else{
                                        Log.e("orderDelivery",bean.get("errmsg").getAsString());
                                    }

                                }else{
                                    Log.e("orderDelivery","Some error happened, Please try again later.");
                                }

                            }, e -> {
                                progressDialog.dismiss();
                                Log.e("orderDelivery","Some error happened, Please try again later.");
                            }
                            )
                    );



    };

}
