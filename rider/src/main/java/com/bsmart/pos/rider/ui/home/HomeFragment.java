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
import android.widget.ListView;
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
import com.bsmart.pos.rider.tools.OrderUtil;
import com.bsmart.pos.rider.views.MainActivity;
import com.bsmart.pos.rider.views.adapter.NewOrderAdapter;
import com.bsmart.pos.rider.views.adapter.OrderAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<OrderBean> orderBeanList;

    @BindView(R.id.orderListView)
    ListView orderListView;

    private NewOrderAdapter orderAdapter;

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

        //add temp tet
        text_home.setOnClickListener(view ->{
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.selectTab(1);
        });


        orderBeanList = new ArrayList<>();
        orderAdapter = new NewOrderAdapter(getContext(),R.layout.order_list_item,orderBeanList);

        checkNewOrder();

        return root;
    }

    private void updateViews(){
        orderListView.setAdapter(orderAdapter);
        if (null != orderBeanList && orderBeanList.size()>0){

            text_home.setText("Have new Jobs!");

        }else{
            text_home.setText("current, no jobs nearby,please wait");
        }

    }

    private void checkNewOrder(){
        orderBeanList.clear();
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
                                                jsonObject.remove("_id");
                                                OrderBean orderBean = App.gson.fromJson(jsonObject,OrderBean.class);
                                                orderBeanList.add(orderBean);
                                            }
                                        }else{

                                            Log.d("HomeFragment","no order nearby");
                                        }
                                        updateViews();
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
