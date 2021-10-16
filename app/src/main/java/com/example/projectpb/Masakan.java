package com.example.projectpb;

import com.example.projectpb.data.Root;
import com.example.projectpb.rest.RestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Masakan{
    public static final String ROOT_URL = "https://masak-apa-tomorisakura.vercel.app/api/recipes";
    private String title;
    private String thumb;
    private String key;
    private String times;
    private String portion;
    private String dificulty;

    Masakan(String title, String key, String thumb, String times, String portion, String dificulty){
        this.title=title;
        this.thumb=thumb;
        this.key=key;
        this.times=times;
        this.portion=portion;
        this.dificulty=dificulty;
    }

    public void getData(){
        //final ProgressDialog loading = ProgressDialog.show(this, "Mengambil Data","Mohon Tunggu",false,false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RestApi service = retrofit.create(RestApi.class);
        Call<Root> call = service.getDataResult();
        call.enqueue(new Callback<Root>(){
            @Override
            public void onResponse(Call<Root>call, Response<Root> response){
                try {
                    title=response.body().getResult().getTitle();
                    thumb=response.body().getResult().getThumb();
                    key=response.body().getResult().getKey();
                    times=response.body().getResult().getTimes();
                    portion=response.body().getResult().getPortion();
                    dificulty=response.body().getResult().getDificulty();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        }
        );
    }

}
