package com.example.projectpb.rest;


import android.provider.DocumentsContract;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {
    @GET("contohjson")
    Call<DocumentsContract.Root> getDataResult();
}
