package com.example.projectpb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hakobastvatsatryan.DropdownTextView;

public class DetailActivity extends AppCompatActivity{
    private String key;
    TextView judul;
    ImageView gambar;
    TextView bahan;
    TextView kesulitanDetail;
    TextView durasiDetail;
    TextView porsiDetail;
    TextView langkah;
    String loading="Loading...";
    DropdownTextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
       //getting key
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        key=extras.getString("key");
        String thumb = extras.getString("thumb");

        Context context = getApplicationContext();
        CharSequence text = key;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //get result
        getDetailResult();
        judul= findViewById(R.id.titleDetail);
        gambar=findViewById(R.id.gbr_makananDetail);
        Glide.with(this).load(thumb).override(1080,1080).into(gambar);
        desc=findViewById(R.id.subTitleDetail);
        desc.setTitleText("Deskripsi Masakan");
        desc.setContentText(loading);
        desc.expand(true);
        bahan= findViewById(R.id.ingredientDetail);
        bahan.setText(loading);
        langkah= findViewById(R.id.langkahDetail);
        langkah.setText(loading);
        kesulitanDetail=findViewById(R.id.kesulitanDetail);
        durasiDetail=findViewById(R.id.durasiDetail);
        porsiDetail=findViewById(R.id.porsiDetail);

    }
    private void getDetailResult() {
        String baseURL = "https://masak-apa-tomorisakura.vercel.app/api/recipe/"+key+"/";
        AndroidNetworking.get(baseURL).setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                try {
                    JSONObject result=response.getJSONObject("results");
                    judul.setText(result.getString("title"));
                    desc.setContentText(result.getString("desc"));
                    String mKesulitan="Tingkat Kesulitan : "+result.getString("dificulty");
                    String mDurasi="Durasi : "+result.getString("times");
                    String mPorsi="Porsi : "+result.getString("servings");
                    durasiDetail.setText(mDurasi);
                    kesulitanDetail.setText(mKesulitan);
                    porsiDetail.setText(mPorsi);
                    //bahan-bahan
                    ArrayList<String> listdata = new ArrayList<>();
                    JSONArray ingr;
                    ingr=result.getJSONArray("ingredient");
                    for(int i=0;i<ingr.length();i++){
                        listdata.add(ingr.getString(i));
                    }
                    StringBuilder sb=new StringBuilder();
                    for(int i=0;i<listdata.size();i++){
                        sb.append("\u2022 "+listdata.get(i)+"\n");
                    }
                    bahan.setText(sb.toString());
                    //langkah langkah
                    ArrayList<String>listStep=new ArrayList<>();
                    JSONArray step;
                    step=result.getJSONArray("step");
                    for(int i=0;i<step.length();i++){
                        listStep.add(step.getString(i));
                    }
                    StringBuilder sb2=new StringBuilder();
                    for(int i=0;i<listStep.size();i++){
                        sb2.append(listStep.get(i)+"\n"+"\n");
                    }
                    langkah.setText(sb2.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(ANError anError) {
                Log.d("onFailure", anError.getMessage());
            }
        });
    }
}