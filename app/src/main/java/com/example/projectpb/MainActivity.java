package com.example.projectpb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectpb.data.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private MasakanAdapter mAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new MasakanAdapter();
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        getResult();
    }
    private void getResult(){
        final ArrayList<Result>listResult=new ArrayList<>();
        String baseURL="https://masak-apa-tomorisakura.vercel.app/api/recipes/";
        AndroidNetworking.get(baseURL).setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                try{
                    JSONArray jsonArray=response.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject result = jsonArray.getJSONObject(i);
                        Result itemResult=new Result();
                        itemResult.setTitle(result.getString("title"));
                        itemResult.setThumb(result.getString("thumb"));
                        listResult.add(itemResult);
                    }
                    mAdapter.setData(listResult);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(ANError anError) {
                Log.d("onFailure",anError.getMessage());
            }
        });
    }

    public void showDetail(View view){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);
    }
}