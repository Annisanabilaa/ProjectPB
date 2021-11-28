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

    @Query("SELECT * from tabel_riwayat ORDER BY `key`ASC")
    List<Riwayat> getAllRiwayat();

    @Query("DELETE FROM tabel_riwayat")
    void deleteAll();

    @Delete
    void deleteRiwayat(Riwayat riwayat);

}
