package com.example.projectpb.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="tabel_riwayat")
public class Riwayat {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    public String mKey;

    @ColumnInfo(name="judul")
    public String mJudul;

    @ColumnInfo(name = "gambar")
    public String mGambar;

    public Riwayat(@NonNull String key, String judul, String gambar){
        this.mKey=key;
        this.mJudul=judul;
        this.mGambar=gambar;
    }

    public String getmJudul() {
        return mJudul;
    }

    public void setmJudul(String mJudul) {
        this.mJudul = mJudul;
    }

    public String getmGambar() {
        return mGambar;
    }

    public void setmGambar(String mGambar) {
        this.mGambar = mGambar;
    }

    @NonNull
    public String getmKey() {
        return mKey;
    }

    public void setmKey(@NonNull String mKey) {
        this.mKey = mKey;
    }

}
