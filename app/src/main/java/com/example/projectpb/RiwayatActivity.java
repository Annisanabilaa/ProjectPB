package com.example.projectpb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.projectpb.data.Riwayat;
import com.example.projectpb.data.RiwayatRoomDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class RiwayatActivity extends AppCompatActivity implements RiwayatListAdapter.OnItemClickCallback{
    private RiwayatListAdapter mAdapter;
    public RiwayatRoomDatabase riwayatdatabase;
    public ArrayList<Riwayat> daftarRiwayat;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Riwayat Bacaan");
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView_riwayat);
        riwayatdatabase = Room.databaseBuilder(getApplicationContext(), RiwayatRoomDatabase
                .class, "riwayat_database").allowMainThreadQueries().build();

        daftarRiwayat = new ArrayList<>(riwayatdatabase.riwayatDao().getAllRiwayat());
        LinearLayoutManager mLayoutManager;
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView emptyView;
        emptyView=findViewById(R.id.empty_view);

        mAdapter=new RiwayatListAdapter(daftarRiwayat, RiwayatActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickCallback(this);

        mRecyclerView.setHasFixedSize(true);

        if(daftarRiwayat.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_riwayat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            Toast.makeText(this, R.string.clear_data_toast_text, Toast.LENGTH_LONG).show();
           riwayatdatabase.riwayatDao().deleteAllRiwayat(daftarRiwayat);
            mAdapter.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(Riwayat data) {
        String key = data.getmKey();
        String thumb = data.getmGambar();
        Intent intent = new Intent(RiwayatActivity.this, DetailActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("thumb", thumb);

        startActivity(intent);
    }


}

