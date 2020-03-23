package com.bsmart.pos.rider.ui.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseFragment;
import com.bsmart.pos.rider.base.BaseQRCodeFragment;
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.api.bean.AddressBean;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.api.enums.PostTypeConstant;
import com.bsmart.pos.rider.base.api.enums.SizeWeightConstant;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.views.MainActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Post
 */
public class HomeFragment extends BaseQRCodeFragment {

    private HomeViewModel homeViewModel;
    Unbinder unbinder;



    @BindView(R.id.header)
    HeaderView header;

    @BindView(R.id.text_home)
    TextView text_home;
    @BindView(R.id.customFromInfo)
    TextView customFromInfo;
    @BindView(R.id.callFromInfo)
    TextView callFromInfo;
    @BindView(R.id.addressFromInfo)
    TextView addressFromInfo;
    @BindView(R.id.fromNav)
    ImageView fromNav;

    @BindView(R.id.customerToInfo)
    TextView customerToInfo;
    @BindView(R.id.callToInfo)
    TextView callToInfo;
    @BindView(R.id.addressToInfo)
    TextView addressToInfo;
    @BindView(R.id.toNav)
    ImageView toNav;

    @BindView(R.id.createTime)
    TextView createTime;
    @BindView(R.id.orderNo)
    TextView orderNo;
    @BindView(R.id.pickTime)
    TextView pickTime;
    @BindView(R.id.postType)
    TextView postType;
    @BindView(R.id.sizeWeight)
    TextView sizeWeight;

    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnAccept)
    Button btnAccept;

    @BindView(R.id.orderZone)
    CardView orderZone;

    private OrderBean orderBean;
    ProgressDialog progressDialog;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        header.setTitle(getResources().getString(R.string.title_post));
        View customRightView = LayoutInflater.from(getContext()).inflate(R.layout.action_right, null);
        customRightView.findViewById(R.id.flRefresh).setOnClickListener(v -> {
            performQRCode();
        });
        header.setRightCustomView(customRightView);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        fromNav.setOnClickListener(view ->{
            Map<String,Double> currentLocation = App.getLocationData();
            if (null != currentLocation){
                AddressBean from = orderBean.getFrom();

                String saddr = currentLocation.get("latitude")+","+currentLocation.get("longitude");
                String daddr = from.getLoc().getLat()+","+from.getLoc().getLon();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?"
                                + "saddr="+saddr
                                + "&daddr="+daddr
                                +"&hl=en")
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER );
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);
            }else {
                ToastUtils.showShort("Location is failed!");
            }
        });

        callFromInfo.setOnClickListener(view ->{
            AddressBean from = orderBean.getFrom();
            String telephone = from.getTelephone();
            Uri telUri = Uri.parse("tel:"+telephone);
            Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
            startActivity(intent);
        });


        toNav.setOnClickListener(view ->{
            Map<String,Double> currentLocation = App.getLocationData();
            if (null != currentLocation){
                AddressBean to = orderBean.getTo();

                String saddr = currentLocation.get("latitude")+","+currentLocation.get("longitude");
                String daddr = to.getLoc().getLat()+","+to.getLoc().getLon();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?"
                                + "saddr="+saddr
                                + "&daddr="+daddr
                                +"&hl=en")
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER );
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);
            }else {
                ToastUtils.showShort("Location is failed!");
            }
        });

        callToInfo.setOnClickListener(view ->{
            AddressBean to = orderBean.getTo();
            String telephone = to.getTelephone();
            Uri telUri = Uri.parse("tel:"+telephone);
            Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
            startActivity(intent);
        });

        btnCancel.setOnClickListener(view ->{

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.selectTab(2);

        });

        btnAccept.setOnClickListener(view ->{

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Processing... Please wait.");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            String orderNo = orderBean.getOrderNo();
            Map<String,Object> requestData = new HashMap<>();
            requestData.put("token", ProfileUtils.getToken());
            requestData.put("orderNo", orderNo);

            Api.getRectsEA().orderAccept(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {
                        progressDialog.dismiss();
                                if (null != bean){

                                    Log.d("orderAccept",bean.toString());

                                    if (bean.get("errno").getAsInt()==0){
                                        orderBean = null;
                                        updateViews();
                                       MainActivity mainActivity = (MainActivity) getActivity();
                                       mainActivity.selectTab(1);
                                    }else{
                                        Log.e("HomeFragment",bean.get("errmsg").getAsString());
                                    }

                                }else{
                                    Log.e("HomeFragment","Some error happened, Please try again later.");
                                }

                            }, e -> {
                        progressDialog.dismiss();
                                Log.e("HomeFragment","Some error happened, Please try again later.");
                            }
                            )
                    );

        });

        checkNewOrder();

        return root;
    }

    private void updateViews(){

        if (null != orderBean){
            orderZone.setVisibility(View.VISIBLE);

            text_home.setText("Have new post order!");

            AddressBean from = orderBean.getFrom();
            customFromInfo.setText(from.getName()+","+from.getTelephone());
            addressFromInfo.setText(from.getPostcode()+","+from.getZone()+","+from.getDetail());

            AddressBean to = orderBean.getTo();
            customerToInfo.setText(to.getName()+","+to.getTelephone());
            addressToInfo.setText(to.getPostcode()+","+to.getZone()+","+to.getDetail());

            createTime.setText("Create Time: "+orderBean.getCreatedDate());
            orderNo.setText("Tracking Num: "+orderBean.getOrderNo());
            pickTime.setText("Pickup Time: "+orderBean.getPickupTime());
            postType.setText("Post Type: "+PostTypeConstant.getInstance().TYPE_ENUM.get(orderBean.getPostType()));
            sizeWeight.setText("Size Weight: "+orderBean.getSizeWeight().toString()+"KG");

        }else{
            text_home.setText("Have no order nearby");
            orderZone.setVisibility(View.GONE);
        }

    }

    private void checkNewOrder(){

        Map<String,Double> location = App.getLocationData();
        if (null != location){
            Map<String,Double> requestData = new HashMap<>();
            requestData.put("lat", location.get("latitude"));
            requestData.put("lon", location.get("longitude"));
            //附近的订单（状态为waiting)
            Api.getRectsEA().nearby(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {

                                if (null != bean){

                                    Log.d("nearby",bean.toString());

                                    if (bean.get("errno").getAsInt()==0){

                                        JsonArray jsonArray = bean.get("data").getAsJsonArray();

                                        if (null != jsonArray && jsonArray.size()>0){
                                            for (int i=0;i<jsonArray.size();i++) {
                                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                                orderBean = new OrderBean();
                                                orderBean.setOrderNo(jsonObject.get("orderNo").getAsString());
                                                orderBean.setFrom(App.gson.fromJson(jsonObject.get("from"),AddressBean.class));
                                                orderBean.setTo(App.gson.fromJson(jsonObject.get("to"),AddressBean.class));
                                                orderBean.setCreatedDate(jsonObject.get("createdDate").getAsString());
                                                orderBean.setPostType(jsonObject.get("postType").getAsInt());
                                                orderBean.setSizeWeight(jsonObject.get("sizeWeight").getAsDouble());
                                                orderBean.setPickupTime(jsonObject.get("pickupTime").getAsString());
                                                updateViews();
                                                break;
                                            }
                                        }else{
                                            updateViews();
                                            Log.d("HomeFragment","no order nearby");
                                        }

                                    }else{
                                        Log.e("HomeFragment",bean.get("errmsg").getAsString());
                                    }

                                }else{
                                    Log.e("HomeFragment","Some error happened, Please try again later.");
                                }

                            }, e -> {

                                Log.e("HomeFragment","Some error happened, Please try again later.");
                            }
                            )
                    );

        }else{
            Log.d("MainActivity","Location is null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
