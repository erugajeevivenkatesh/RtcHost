package com.example.surya.rtctracker;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 7/19/2017.
 */

public interface Sendreset {
    @FormUrlEncoded
    @POST("/sendmainotp.php")
    void insertUser(
            @Field("email") String email,

            Callback<Response> callback);
}