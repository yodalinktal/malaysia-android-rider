package com.bsmart.pos.rider.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.BaseFragment;
import com.bsmart.pos.rider.base.api.bean.AddressBean;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.views.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: yoda
 * DateTime: 2020/3/7 17:19
 */
public class OrderAcceptedFragment extends BaseFragment {

    private View rootView;
    Unbinder unbinder;

    private List<OrderBean> orderBeanList;

    @BindView(R.id.orderListView)
    ListView orderListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_accepted, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        initOrderBeanList();
        OrderAdapter orderAdapter = new OrderAdapter(getContext(),R.layout.order_list_item,orderBeanList);
        orderListView.setAdapter(orderAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initOrderBeanList(){
        orderBeanList = new ArrayList<OrderBean>();

        String orderNo = "23242342";
        OrderBean orderBean = new OrderBean();
        orderBean.setOrderNo(orderNo);


        orderBean.setSizeWeight(0);
        orderBean.setPickupTime("2020-02-10 10:10");
        orderBean.setPayType(1);
        orderBean.setPostType(1);

        AddressBean from = new AddressBean();
        from.setDetail("Section 51A, Petaling Jaya,46100 Selangor.");
        from.setZone("Axis Business Campus,No 13A &13B Jalan 225,");
        from.setTelephone("18601361839");
        from.setName("yoda");
        from.setLat(39.509805);
        from.setLon(116.4105069);
        orderBean.setFrom(from);

        AddressBean to = new AddressBean();
        to.setDetail("Section 61A, Petaling Jaya,46100 Selangor.");
        to.setZone("Axis Business Campus,No 13A &13B Jalan 225,");
        to.setTelephone("601012843322");
        to.setName("st");
        to.setLat(39.509805);
        to.setLon(116.4105069);
        orderBean.setTo(to);

        orderBean.setCreateTime("2020-02-10 12:00");
        orderBean.setStatus("Waiting");
        orderBeanList.add(orderBean);

    }

}
