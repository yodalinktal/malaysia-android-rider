package com.bsmart.pos.rider.views.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.api.bean.AddressBean;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.api.enums.OrderStatusConstant;
import com.bsmart.pos.rider.base.api.enums.PostTypeConstant;
import com.bsmart.pos.rider.base.api.enums.SizeWeightConstant;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.tools.DateUtil;
import com.bsmart.pos.rider.tools.OrderUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: yoda
 * DateTime: 2020/3/5 22:44
 */
public class OrderAdapter extends ArrayAdapter<OrderBean> {

    private int resourceId;
    ProgressDialog progressDialog;

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<OrderBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderBean orderBean = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.customFromInfo = (TextView) view.findViewById(R.id.customFromInfo);
            viewHolder.addressFromInfo = (TextView) view.findViewById(R.id.addressFromInfo);
            viewHolder.customerToInfo = (TextView) view.findViewById(R.id.customerToInfo);
            viewHolder.addressToInfo = (TextView) view.findViewById(R.id.addressToInfo);
            viewHolder.createTime = (TextView) view.findViewById(R.id.createTime);
            viewHolder.orderNo = (TextView) view.findViewById(R.id.orderNo);
            viewHolder.pickTime = (TextView) view.findViewById(R.id.pickTime);
            viewHolder.postType = (TextView) view.findViewById(R.id.postType);
            viewHolder.sizeWeight = (TextView) view.findViewById(R.id.sizeWeight);
            viewHolder.volume = (TextView) view.findViewById(R.id.volume);
            viewHolder.btnOperation = (Button) view.findViewById(R.id.btnOperation);

            viewHolder.callFromInfo = (TextView) view.findViewById(R.id.callFromInfo);
            viewHolder.callToInfo = (TextView) view.findViewById(R.id.callToInfo);
            viewHolder.fromNav = (ImageView) view.findViewById(R.id.fromNav);
            viewHolder.toNav = (ImageView) view.findViewById(R.id.toNav);

            view.setTag(viewHolder);
        }else{
            view = convertView;
            // 取出缓存
            viewHolder = (ViewHolder) view.getTag();
        }

        if (orderBean.getStatus().equals(OrderStatusConstant.DELIVERING)){
            viewHolder.btnOperation.setVisibility(View.VISIBLE);
            viewHolder.btnOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deliveryOrder(orderBean, OrderAdapter.this);

                }
            });
        }else{
            viewHolder.btnOperation.setVisibility(View.GONE);
            viewHolder.btnOperation.setOnClickListener(null);
        }


        viewHolder.fromNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    getContext().startActivity(intent);
                }else{
                    ToastUtils.showShort("Location is failed!");
                }

            }
        });

        viewHolder.toNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    getContext().startActivity(intent);
                }else {
                    ToastUtils.showShort("Location is failed!");
                }
            }
        });

        viewHolder.callFromInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressBean from = orderBean.getFrom();
                String telephone = from.getTelephone();
                Uri telUri = Uri.parse("tel:"+telephone);
                Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                getContext().startActivity(intent);
            }
        });

        viewHolder.callToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressBean to = orderBean.getTo();
                String telephone = to.getTelephone();
                Uri telUri = Uri.parse("tel:"+telephone);
                Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                getContext().startActivity(intent);
            }
        });

        viewHolder.customFromInfo.setText(orderBean.getFrom().getName()+" "+orderBean.getFrom().getTelephone());
        viewHolder.addressFromInfo.setText(orderBean.getFrom().getPostcode()+","+orderBean.getFrom().getDetail()+","+orderBean.getFrom().getCity()+","+orderBean.getFrom().getState());
        viewHolder.customerToInfo.setText(orderBean.getTo().getName()+" "+orderBean.getTo().getTelephone());
        viewHolder.addressToInfo.setText(orderBean.getTo().getPostcode()+","+orderBean.getTo().getDetail()+","+orderBean.getTo().getCity()+","+orderBean.getTo().getState());
        viewHolder.createTime.setText("Post Time: "+ DateUtil.formatTimestampEnglish(orderBean.getCreatedTimestamp()));
        viewHolder.orderNo.setText("Tracking Num: "+ OrderUtil.formatOrderNo(orderBean.getOrderNo()));
        viewHolder.pickTime.setText("Estimate Time: "+orderBean.getPickupTime());
        viewHolder.postType.setText("Post Type: "+ PostTypeConstant.getInstance().TYPE_ENUM.get(orderBean.getPostType()));
        viewHolder.sizeWeight.setText("Size Weight: "+ orderBean.getSizeWeight()+"KG");

        if (null != orderBean.getVolume_length()){
            viewHolder.volume.setVisibility(View.VISIBLE);
            viewHolder.volume.setText("Volume: "+orderBean.getVolume_length()
                    +"x"+
                    orderBean.getVolume_width()
                    +"x"+
                    orderBean.getVolume_height()
                    +"CM");
        }else{
            viewHolder.volume.setVisibility(View.GONE);
        }

        return view;
    }

    private void deliveryOrder(final OrderBean orderBean,final OrderAdapter orderAdapter){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Info");
        builder.setMessage("Are you sure delivery to DC?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Processing... Please wait.");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Map<String,Object> requestData = new HashMap<>();
                requestData.put("orderNo", orderBean.getOrderNo());
                requestData.put("token", ProfileUtils.getToken());
                //附近的订单（状态为waiting)
                Api.getRectsEA().orderFinished(requestData)
                        .compose(new NetTransformer<>(JsonObject.class))
                        .subscribe(new NetSubscriber<>(bean -> {
                            progressDialog.dismiss();
                                    if (null != bean){

                                        Log.d("orderFinished",bean.toString());

                                        if (bean.get("errno").getAsInt()==0){
                                            orderAdapter.remove(orderBean);
                                            orderAdapter.notifyDataSetChanged();
                                        }else{
                                            Log.e("OrderFragment",bean.get("errmsg").getAsString());
                                        }

                                    }else{
                                        Log.e("OrderFragment","Some error happened, Please try again later.");
                                    }

                                }, e -> {
                                    progressDialog.dismiss();
                                    Log.e("OrderFragment","Some error happened, Please try again later.");
                                }
                                )
                        );

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    // 内部类
    class ViewHolder{

        TextView customFromInfo;
        TextView addressFromInfo;
        TextView customerToInfo;
        TextView addressToInfo;
        TextView createTime;
        TextView orderNo;
        TextView pickTime;
        TextView postType;
        TextView sizeWeight;
        TextView volume;
        Button btnOperation;

        TextView callFromInfo;
        TextView callToInfo;
        ImageView fromNav;
        ImageView toNav;

    }
}
