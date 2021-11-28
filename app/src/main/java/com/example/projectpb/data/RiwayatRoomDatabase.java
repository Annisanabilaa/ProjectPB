package com.example.projectpb.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Riwayat.class}, version = 1, exportSchema = false)
public abstract class RiwayatRoomDatabase extends RoomDatabase {
    public abstract RiwayatDao riwayatDao();

    private static RiwayatRoomDatabase INSTANCE;

    public static RiwayatRoomDatabase getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (RiwayatRoomDatabase.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RiwayatRoomDatabase.class, "riwayat_database")
                            .build();
                }
            }
        }
    return INSTANCE;
    }
}
