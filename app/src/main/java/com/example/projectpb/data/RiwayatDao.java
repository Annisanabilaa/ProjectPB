package com.example.projectpb.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RiwayatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRiwayat(Riwayat riwayat);

    @Query("SELECT * FROM tabel_riwayat")
    List<Riwayat> getAllRiwayat();

    @Delete
    void deleteAllRiwayat(List<Riwayat> riwayat);

    @Delete
    void deleteRiwayat(Riwayat riwayat);

}
