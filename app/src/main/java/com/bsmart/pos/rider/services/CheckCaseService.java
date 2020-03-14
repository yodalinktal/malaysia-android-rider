package com.bsmart.pos.rider.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.base.utils.notification.NotifyUtil;
import com.bsmart.pos.rider.tools.StringUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CheckCaseService extends Service {
    private final ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        Notification n = NotifyUtil.getNotification(this, "POSRider is running", true, NotifyUtil.getPendingIntent(this));
        this.startForeground(1, n);
        thread.scheduleAtFixedRate(netWorker, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Runnable netWorker = new Runnable() {

        @Override
        public void run() {

            Map<String, Double> locationData = App.getLocationData();
            //API
//            //todo:不断更新骑手位置
//            String token = ProfileUtils.getToken();
//            Log.d("CheckService","token:"+token+",locationData:"+locationData);
//            if (StringUtil.isNotEmpty(token) && null != locationData){
//                Map<String, Object> requestData = new HashMap<>();
//                requestData.put("token", token);
//                requestData.put("lon", locationData.get("longitude"));
//                requestData.put("lat", locationData.get("latitude"));
//                Api.getRectsEA().traceUpdate(requestData)
//                        .compose(new NetTransformer<>(JsonObject.class))
//                        .subscribe(new NetSubscriber<>(bean -> {
//
//                                    if (null != bean){
//
//                                        if (bean.get("errno").getAsInt()==0){
//                                            Log.d("LocationUtils Update:","Success");
//                                        }else{
//                                            Log.e("LocationUtils Error:",bean.get("errmsg").getAsString());
//                                        }
//
//                                    }else{
//                                        Log.e("LocationUtils Error:","Some error happened, Please try again later");
//                                    }
//
//                                }, e -> {
//                                    Log.e("LocationUtils Error:","Some error happened, Please try again later");
//
//                                }
//                                )
//                        );
//            }
        }//end run
    };
}
