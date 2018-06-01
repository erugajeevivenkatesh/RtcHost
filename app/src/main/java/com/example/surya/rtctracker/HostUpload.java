package com.example.surya.rtctracker;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 6/4/2017.
 */

public interface HostUpload {
    @FormUrlEncoded
    @POST("/testingupload.php")
    void insertUser(
            @Field("Fullname") String Fullname,
            @Field("Hostingroute") String Hostingroute,
            @Field("lattitude") String lattitude,
            @Field("longitude") String longitude,
            Callback<Response> callback);}