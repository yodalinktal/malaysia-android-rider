package com.bsmart.pos.rider.views;

import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.addActivity(this);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_order, R.id.navigation_me)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //checkNewOrder();
    }

    public void selectTab(int ItemId){
        if (null != navView){
            NavigationUI.onNavDestinationSelected(navView.getMenu().getItem(ItemId),navController);
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
                                                JsonObject orderBean = jsonArray.get(i).getAsJsonObject();
                                            }
                                        }else{

                                            Log.d("MainActivity","no order nearby");
                                        }

                                    }else{
                                        Log.e("MainActivity",bean.get("errmsg").getAsString());
                                    }

                                }else{
                                    Log.e("MainActivity","Some error happened, Please try again later.");
                                }

                            }, e -> {

                                    Log.e("MainActivity","Some error happened, Please try again later.");
                            }
                            )
                    );

        }else{
            Log.d("MainActivity","Location is null");
        }
    }

}
