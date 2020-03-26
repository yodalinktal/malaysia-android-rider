package com.bsmart.pos.rider.base.api;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface RectsEAService {

    @FormUrlEncoded
    @POST("rider/register")
    Observable<Response<JsonObject>> register(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rider/login")
    Observable<Response<JsonObject>> login(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rider/change/password")
    Observable<Response<JsonObject>> changePassword(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("customer/reset/password")
    Observable<Response<JsonObject>> resetPassword(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rider/validate")
    Observable<Response<JsonObject>> validate(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rider/trace/update")
    Observable<Response<JsonObject>> traceUpdate(@FieldMap Map<String, Object> meta);


    @FormUrlEncoded
    @POST("rider/order/nearby")
    Observable<Response<JsonObject>> nearby(@FieldMap Map<String, Double> meta);

    @FormUrlEncoded
    @POST("rider/order/list")
    Observable<Response<JsonObject>> orderList(@FieldMap Map<String, Object> meta);


    @FormUrlEncoded
    @POST("rider/order/accept")
    Observable<Response<JsonObject>> orderAccept(@FieldMap Map<String, Object> meta);

    @FormUrlEncoded
    @POST("rider/order/delivery")
    Observable<Response<JsonObject>> orderDelivery(@FieldMap Map<String, Object> meta);

    @FormUrlEncoded
    @POST("rider/order/finished")
    Observable<Response<JsonObject>> orderFinished(@FieldMap Map<String, Object> meta);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(Url url);

}
