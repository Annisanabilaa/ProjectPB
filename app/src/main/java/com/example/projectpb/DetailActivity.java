package com.example.projectpb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.projectpb.data.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private String key;
    TextView judul;
    ImageView gambar;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
       //getting key
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        key=extras.getString("key");


        Context context = getApplicationContext();
        CharSequence text = key;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //get result
        getDetailResult();
        judul= findViewById(R.id.titleDetail);
        gambar=findViewById(R.id.gbr_makananDetail);
        desc=findViewById(R.id.subTitleDetail);
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
                    Glide.with(getApplication()).load(result.getString("thumb")).override(1080,1080).into(gambar);
                    desc.setText(result.getString("desc"));
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