package com.example.projectpb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        // Get the intent and its data.
        //Intent intent = getIntent();
        setContentView(R.layout.activity_riwayat);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Riwayat Bacaan");
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView_riwayat);
        RiwayatRoomDatabase database = Room.databaseBuilder(getApplicationContext(), RiwayatRoomDatabase
                .class, "riwayat_database").allowMainThreadQueries().build();

        daftarRiwayat = new ArrayList<>(database.riwayatDao().getAllRiwayat());
        LinearLayoutManager mLayoutManager;
        mLayoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new RiwayatListAdapter(daftarRiwayat, RiwayatActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickCallback(this::onItemClicked);
        mRecyclerView.setHasFixedSize(true);

        /*ItemTouchHelper helper=new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position=viewHolder.getBindingAdapterPosition();
                        Riwayat swipeRiwayat=mAdapter.getRiwayatAtPosition(position);
                        removeItem(swipeRiwayat);
                    }
                });helper.attachToRecyclerView(mRecyclerView);*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_riwayat, menu);
        return true;
    }

    // The options menu has a single item "Clear all data now"
    // that deletes all the entries in the database
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText(this, R.string.clear_data_toast_text, Toast.LENGTH_LONG).show();

            // Delete the existing data
           // daftarRiwayat=riwayatdatabase.riwayatDao().deleteAll();
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

