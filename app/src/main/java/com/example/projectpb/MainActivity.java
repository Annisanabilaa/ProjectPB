package com.example.projectpb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Masakan> mMasakanData;
    private MasakanAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mMasakanData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new MasakanAdapter(this, mMasakanData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();

    }


    public void show_detail(View view) {
        Intent intent =
                new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] masakanList = getResources()
                .getStringArray(R.array.judul_masakan);
        String[] masakanInfo = getResources()
                .getStringArray(R.array.info_masakan);

        TypedArray masakanImageResources =
                getResources().obtainTypedArray(R.array.gbr_makanan);

        // Clear the existing data (to avoid duplication).
        mMasakanData.clear();

        // Create the ArrayList of Sports objects with titles and
        // information about each sport.
        for(int i=0;i<masakanList.length;i++){
            mMasakanData.add(new Masakan(masakanList[i],masakanInfo[i],
                    masakanImageResources.getResourceId(i,0)));
        }


        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();

        masakanImageResources.recycle();

    }
}