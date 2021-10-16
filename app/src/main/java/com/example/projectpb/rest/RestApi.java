package com.example.projectpb.rest;

import com.example.projectpb.data.Root;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {

    @GET("contohjson")
    Call<Root> getDataResult();
}
