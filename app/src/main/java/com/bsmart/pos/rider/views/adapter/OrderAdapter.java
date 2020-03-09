package com.bsmart.pos.rider.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.api.enums.PostTypeConstant;
import com.bsmart.pos.rider.base.api.enums.SizeWeightConstant;

import java.util.List;

/**
 * Author: yoda
 * DateTime: 2020/3/5 22:44
 */
public class OrderAdapter extends ArrayAdapter<OrderBean> {

    private int resourceId;

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
            view.setTag(viewHolder);
        }else{
            view = convertView;
            // 取出缓存
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.customFromInfo.setText(orderBean.getFrom().getName()+","+orderBean.getFrom().getTelephone());
        viewHolder.addressFromInfo.setText(orderBean.getFrom().getZone()+" "+orderBean.getFrom().getDetail());
        viewHolder.customerToInfo.setText(orderBean.getTo().getName()+","+orderBean.getTo().getTelephone());
        viewHolder.addressToInfo.setText(orderBean.getTo().getZone()+" "+orderBean.getTo().getDetail());
        viewHolder.createTime.setText("createTime: "+orderBean.getCreateTime());
        viewHolder.orderNo.setText("orderNo: "+orderBean.getOrderNo());
        viewHolder.pickTime.setText("pickTime: "+orderBean.getPickupTime());
        viewHolder.postType.setText("postType: "+ PostTypeConstant.getInstance().TYPE_ENUM.get(orderBean.getPostType()));
        viewHolder.sizeWeight.setText("sizeWeight: "+ SizeWeightConstant.getInstance().TYPE_ENUM.get(orderBean.getSizeWeight()));

        return view;
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

    }
}
