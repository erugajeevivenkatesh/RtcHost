package com.example.surya.rtctracker;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 6/4/2017.
 */

public interface Remove {
    @FormUrlEncoded
    @POST("/Remove.php")
    void insertUser(
            @Field("name") String name,
            Callback<Response> callback);
}
