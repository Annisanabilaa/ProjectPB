package com.example.projectpb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectpb.data.Riwayat;
import com.example.projectpb.data.RiwayatRoomDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RiwayatListAdapter extends RecyclerView.Adapter<RiwayatListAdapter.RiwayatViewHolder> {
    private ArrayList<Riwayat> daftarRiwayat;
    private RiwayatRoomDatabase riwayatDatabase;
    private RiwayatListAdapter.OnItemClickCallback onItemClickCallback;
    private Context context;

    public RiwayatListAdapter(ArrayList<Riwayat> daftarRiwayat, Context context){
        this.daftarRiwayat=daftarRiwayat;
        this.context=context;
        riwayatDatabase= Room.databaseBuilder(context.getApplicationContext(),RiwayatRoomDatabase.class,
                "riwayat_database").allowMainThreadQueries().build();
    }

    public void setOnItemClickCallback(RiwayatListAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Riwayat> items) {
        //mData.clear();
        daftarRiwayat.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RiwayatListAdapter.RiwayatViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
        return new RiwayatViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatListAdapter.RiwayatViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String getKey=daftarRiwayat.get(position).getmKey();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] menuPilihan = {"Hapus"};
                AlertDialog.Builder dialog=new AlertDialog.Builder(v.getContext())
                        .setTitle("Hapus Riwayat Bacaan ini ?")
                        .setItems(menuPilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    onDeleteData(position);
                                    break;
                            }
                        }
                });
                dialog.create();
                dialog.show();
                return true;
            }
        });

        holder.bind(daftarRiwayat.get(position));
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(daftarRiwayat.get(holder.getBindingAdapterPosition())));
    }

    private void onDeleteData(int position){
        riwayatDatabase.riwayatDao().deleteRiwayat(daftarRiwayat.get(position));
        daftarRiwayat.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,daftarRiwayat.size());
    }

    @Override
    public int getItemCount(){
        return daftarRiwayat.size();
    }

    static class RiwayatViewHolder extends RecyclerView.ViewHolder {
        TextView judul;
        ImageView gambar;
        String key;

        public RiwayatViewHolder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.title_riwayat);
            gambar = itemView.findViewById(R.id.gbr_riwayat);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v -> getBindingAdapterPosition());
        }


        void bind(Riwayat riwayat){
            Glide.with(itemView.getContext())
                    .load(riwayat.getmGambar())
                    .apply(new RequestOptions().override(1920, 1920))
                    .into(gambar);
            judul.setText(riwayat.getmJudul());
            key=riwayat.getmKey();
        }
    }

    void setRiwayat(ArrayList<Riwayat> riwayat) {
        daftarRiwayat = riwayat;
        notifyDataSetChanged();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Riwayat data);
    }

    public Riwayat getRiwayatAtPosition (int position) {
        return daftarRiwayat.get(position);
    }
}