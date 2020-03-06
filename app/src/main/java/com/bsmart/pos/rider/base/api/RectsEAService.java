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
    @POST("rru/account/login")
    Observable<Response<JsonObject>> login(@FieldMap Map<String, String> meta);


    @FormUrlEncoded
    @POST("rru/app/order/available")
    Observable<Response<JsonObject>> availableCases(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rru/app/dashboard")
    Observable<Response<JsonObject>> toggleDuty(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rru/app/order/details")
    Observable<Response<JsonObject>> orderDetails(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rru/app/order/update")
    Observable<Response<JsonObject>> updateCaseStatus(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rru/app/order/bid")
    Observable<Response<JsonObject>> acceptCase(@FieldMap Map<String, String> meta);

    @FormUrlEncoded
    @POST("rru/app/order/bid/ack")
    Observable<Response<JsonObject>> confirmAcceptCase(@FieldMap Map<String, String> meta);

    @Multipart
    @POST("rru/app/order/comment")
    Observable<Response<JsonObject>> uploadComment(@PartMap Map<String, RequestBody> body, @Part MultipartBody.Part[] media);

    @FormUrlEncoded
    @POST("rru/app/order/getComments")
    Observable<Response<JsonObject>> getComments(@FieldMap Map<String, String> meta);


    @Streaming
    @GET
    Call<ResponseBody> downloadFile(Url url);

}
