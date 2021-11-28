package com.example.projectpb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectpb.data.Result;
import com.example.projectpb.data.Riwayat;
import com.example.projectpb.data.RiwayatRoomDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MasakanAdapter.OnItemClickCallback{
    private MasakanAdapter mAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RiwayatRoomDatabase database;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager;
        mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MasakanAdapter();
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        getResult();
        mAdapter.setOnItemClickCallback(this::onItemClicked);

        //infinitescroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = 1;
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            loadNextDataFromAPI(totalItemCount);
                            loading = true;
                        }
                    }
                }
            }
        });
        database = Room.databaseBuilder(
                getApplicationContext(),
                RiwayatRoomDatabase.class,
                "riwayat_database") //Nama File Database yang akan disimpan
                .build();

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && fab.isShown())
                {
                    fab.hide();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
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
        //return super.onCreateOptionsMenu(menu);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else {
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;
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

    private void loadNextDataFromAPI(int page){
        final ArrayList<Result> listResult = new ArrayList<>();
        String baseURL = "https://masak-apa-tomorisakura.vercel.app/api/recipes/"+page;
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

    @Override
    public void onItemClicked(Result data) {
            String key = data.getKey();
            String thumb = data.getThumb();
            String title=data.getTitle();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("key", key);
            intent.putExtra("thumb", thumb);

            Riwayat dataRiwayat=new Riwayat(key,title,thumb);
            insertData(dataRiwayat);
            startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertData(final Riwayat riwayat){
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                //Menjalankan proses insert data
                return database.riwayatDao().insertRiwayat(riwayat);
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if the correct item was clicked.
        switch (item.getItemId()) {
            case R.id.riwayat:
                Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
                startActivity(intent);
                return true;
            case R.id.night_mode:
                if (item.getItemId() == R.id.night_mode) {
                    // Get the night mode state of the app.
                    int nightMode = AppCompatDelegate.getDefaultNightMode();
                    // Set the theme mode for the restarted activity.
                    if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode
                                (AppCompatDelegate.MODE_NIGHT_NO);

                    } else {
                        AppCompatDelegate.setDefaultNightMode
                                (AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    recreate();
                }
                return true;
        }

        return false;
    }

}
