package com.kceylan.app.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kceylan.app.dao.TVShowDao;
import com.kceylan.app.models.TVShow;

@Database(entities = {TVShow.class}, version = 4, exportSchema = false)
public abstract class TVShowsDatabase extends RoomDatabase {

    private static TVShowsDatabase tvShowsDatabase;

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };
    public static synchronized TVShowsDatabase getTvShowsDatabase(Context context) {
        if (tvShowsDatabase == null) {
            tvShowsDatabase = Room.databaseBuilder(
                    context,
                    TVShowsDatabase.class,
                    "tv_shows_db"
            )
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                    .build();
        }
        return tvShowsDatabase;
    }

    public abstract TVShowDao tvShowDao();

}
