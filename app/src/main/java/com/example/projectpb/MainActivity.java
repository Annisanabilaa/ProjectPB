package com.example.projectpb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectpb.data.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
    implements MasakanAdapter.OnItemClickCallback{
    private MasakanAdapter mAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MasakanAdapter();
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        getResult();
        mAdapter.setOnItemClickCallback(this::onItemClicked);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Cari Resep...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1){
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                    //Ambildata searcing
                    mAdapter = new MasakanAdapter();
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                    getSearching(newText);
                    mAdapter.setOnItemClickCallback(MainActivity.this::onItemClicked);
                }else if (newText.length() < 1){
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                    mAdapter = new MasakanAdapter();
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                    getResult();
                    mAdapter.setOnItemClickCallback(MainActivity.this::onItemClicked);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getResult() {
        final ArrayList<Result> listResult = new ArrayList<>();
        String baseURL = "https://masak-apa-tomorisakura.vercel.app/api/recipes/1";
        AndroidNetworking.get(baseURL).setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = jsonArray.getJSONObject(i);
                        Result itemResult = new Result();
                        itemResult.setTitle(result.getString("title"));
                        itemResult.setThumb(result.getString("thumb"));
                        itemResult.setTimes(result.getString("times"));
                        itemResult.setDificulty(result.getString("dificulty"));
                        itemResult.setKey(result.getString("key"));
                        listResult.add(itemResult);
                    }
                    mAdapter.setData(listResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d("onFailure", anError.getMessage());
            }
        });
    }


    private void getSearching(String data) {
        final ArrayList<Result> listResult = new ArrayList<>();
        String baseURL = "https://masak-apa-tomorisakura.vercel.app/api/search/?q="+data;
        AndroidNetworking.get(baseURL).setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = jsonArray.getJSONObject(i);
                        Result itemResult = new Result();
                        Log.d("MainActivity", "DATA SEARCHING : " + result);
                        itemResult.setTitle(result.getString("title"));
                        itemResult.setThumb(result.getString("thumb"));
                        itemResult.setTimes(result.getString("times"));
                        itemResult.setDificulty(result.getString("difficulty"));
                        itemResult.setKey(result.getString("key"));
                        listResult.add(itemResult);
                    }
                    mAdapter.setData(listResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d("onFailure", anError.getMessage());
            }
        });
    }

    @Override
    public void onItemClicked(Result data) {
            String key = data.getKey();
            String thumb = data.getThumb();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("key", key);
            intent.putExtra("thumb", thumb);
            startActivity(intent);
    }


}
