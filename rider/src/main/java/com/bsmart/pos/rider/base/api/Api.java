package com.bsmart.pos.rider.base.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private Api() {
    }

    private static OkHttpClient okHttpClient;
    private static RectsEAService rectsEAService;
    public final static String CONTENTKEY = "data";

    static {
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        rectsEAService = new Retrofit.Builder()
                .baseUrl(baseUrl())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RectsEAService.class);
    }

    public static RectsEAService getRectsEA() {
        return rectsEAService;
    }

    public static String baseUrl() {
//        if (BuildConfig.DEBUG) {
//            return "http://47.254.215.143/pos/api/";
//        } else {
//            return "http://47.254.215.143/pos/api/";
//        }
        return "http://47.254.215.143/pos/api/";
    }
}
