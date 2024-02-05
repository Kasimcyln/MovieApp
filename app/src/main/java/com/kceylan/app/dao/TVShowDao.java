package com.kceylan.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kceylan.app.models.TVShow;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvShows")
    Flowable<List<TVShow>> getWatchList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     Completable addWatchList(TVShow tvShow);

    @Delete
    Completable removeFromWatchList(TVShow tvShow);

    @Query("SELECT * FROM tvShows WHERE id = :tvShowId")
    Flowable<TVShow> getTVShowFromWatchlist(String tvShowId);

}
