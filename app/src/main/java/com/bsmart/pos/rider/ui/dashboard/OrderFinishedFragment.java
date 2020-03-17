package com.bsmart.pos.rider.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseFragment;
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.api.bean.OrderBean;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.views.adapter.OrderAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: yoda
 * DateTime: 2020/3/7 17:19
 */
public class OrderFinishedFragment extends BaseFragment {

    private View rootView;
    Unbinder unbinder;

    private List<OrderBean> orderBeanList;

    @BindView(R.id.orderListView)
    ListView orderListView;

    private OrderAdapter orderAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_finished, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        orderBeanList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(),R.layout.order_list_item,orderBeanList);
        initOrderBeanList();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("OrderFinishedFragment","onResume");
    }

    public void refresh(){
        orderBeanList.clear();
        initOrderBeanList();
    }

    private void initOrderBeanList(){

        String token = ProfileUtils.getToken();
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("token", token);
        requestData.put("pageSize", 20);
        requestData.put("page", 1);
        requestData.put("status", OrderFragment.STATUS_FINISHED);

        Api.getRectsEA().orderList(requestData)
                .compose(new NetTransformer<>(JsonObject.class))
                .subscribe(new NetSubscriber<>(bean -> {

                            if (null != bean){

                                Log.d("beanString",bean.toString());

                                if (bean.get("errno").getAsInt()==0){
                                    /**
                                     * {
                                     * 	"errno": 0,
                                     * 	"data": {
                                     * 		"pageSize": 20,
                                     * 		"page": 1,
                                     * 		"list": [],
                                     * 		"totalDataCount": 0,
                                     * 		"totalPage": 0,
                                     * 		"dataCount": 0,
                                     * 		"hasNext": false,
                                     * 		"hasPrevious": false,
                                     * 		"pageMsg": "共0条,共0页,当前1页"
                                     *        },
                                     * 	"errmsg": "OK"
                                     * }
                                     * */
                                    JsonObject data = bean.get("data").getAsJsonObject();
                                    JsonArray jsonArray = data.get("list").getAsJsonArray();

                                    if (null != jsonArray && jsonArray.size()>0){
                                        for (int i=0;i<jsonArray.size();i++) {

                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            OrderBean orderBean = App.gson.fromJson(jsonObject,OrderBean.class);
                                            orderBeanList.add(orderBean);
                                        }
                                    }else{
                                        //no order list
                                    }
                                    orderListView.setAdapter(orderAdapter);
                                }else{
                                    ToastUtils.showShort(bean.get("errmsg").getAsString());
                                }

                            }else{
                                ToastUtils.showShort("Some error happened, Please try again later.");
                            }

                        }, e -> {

                            ToastUtils.showShort("Some error happened, Please try again later.");
                        }
                        )
                );//end api


//        String orderNo = "23242342";
//        OrderBean orderBean = new OrderBean();
//        orderBean.setOrderNo(orderNo);
//
//
//        orderBean.setSizeWeight(0);
//        orderBean.setPickupTime("2020-02-10 10:10");
//        orderBean.setPayType(1);
//        orderBean.setPostType(1);
//
//        AddressBean from = new AddressBean();
//        from.setDetail("Section 51A, Petaling Jaya,46100 Selangor.");
//        from.setZone("Axis Business Campus,No 13A &13B Jalan 225,");
//        from.setTelephone("18601361839");
//        from.setName("yoda");
//        from.setLat(39.509805);
//        from.setLon(116.4105069);
//        orderBean.setFrom(from);
//
//        AddressBean to = new AddressBean();
//        to.setDetail("Section 61A, Petaling Jaya,46100 Selangor.");
//        to.setZone("Axis Business Campus,No 13A &13B Jalan 225,");
//        to.setTelephone("601012843322");
//        to.setName("st");
//        to.setLat(39.509805);
//        to.setLon(116.4105069);
//        orderBean.setTo(to);
//
//        orderBean.setCreateDate("2020-02-10 12:00");
//        orderBean.setStatus("Waiting");
//        orderBeanList.add(orderBean);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
