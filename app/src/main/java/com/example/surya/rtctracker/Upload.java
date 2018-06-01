package com.example.surya.rtctracker;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 6/4/2017.
 */

public interface Upload {
    @FormUrlEncoded
    @POST("/testingupload.php")
    void insertUser(
            @Field(("fullname")) String fullname,
            @Field("hostroute") String hostroute,
            @Field("Longitude") String longitude,
            @Field("Lattitude") String lattitude,
            Callback<Response> callback);
}
