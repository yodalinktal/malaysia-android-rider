package com.bsmart.pos.rider.base;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.PhoneUtils;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: yoda
 * DateTime: 2020/2/29 15:40
 */
public class App extends MultiDexApplication {

    private static List<Activity> activities = new ArrayList<Activity>();

    public static Context ctx;

    public static Gson gson;

    private static Location location;

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void exit(){
        ProfileUtils.removeSharePreference();
        for (Activity activity : activities) {
            if (null == activity || activity.isDestroyed()){
                continue;
            }
            activity.finish();
        }
        System.exit(0);
    }

    public static Map<String, String> getMetaRequestData() {

        HashMap<String, String> map = new HashMap<>();

        try {
            map.put("device_id", PhoneUtils.getDeviceId());
        } catch (SecurityException e) {
        }
        if (location != null) {
            map.put("latitude", String.valueOf(LocationUtils.getInstance(ctx).showLocation().getLatitude()));
            map.put("longitude", String.valueOf(LocationUtils.getInstance(ctx).showLocation().getLongitude()));
        } else {
            map.put("latitude", "0.0");
            map.put("longitude", "0.0");
        }

        return map;
    }

    public static void resetLocation(){
        location = LocationUtils.getInstance(ctx).showLocation();
    }

    public static Map<String,Double> getLocationData(){

        if (null != location){
            HashMap<String,Double> map = new HashMap<>();
            map.put("latitude",LocationUtils.getInstance(ctx).showLocation().getLatitude());
            map.put("longitude",LocationUtils.getInstance(ctx).showLocation().getLongitude());
            return map;
        }else{
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        gson = new Gson();

        location = LocationUtils.getInstance(this).showLocation();

        Stetho.initializeWithDefaults(this);
        // ExceptionUtils.getINSTANCE().initialize(this);

        ZXingLibrary.initDisplayOpinion(this);
        CrashReport.initCrashReport(this);
    }

    public static App getApp() {
        return (App) ctx;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
