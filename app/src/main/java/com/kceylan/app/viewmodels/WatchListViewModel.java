package com.kceylan.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.kceylan.app.database.TVShowsDatabase;
import com.kceylan.app.models.TVShow;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class WatchListViewModel extends AndroidViewModel {

    private final TVShowsDatabase tvShowsDatabase;


    public WatchListViewModel(@NonNull Application application) {
        super(application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public Flowable<List<TVShow>> loadWatchList() {
        return tvShowsDatabase.tvShowDao().getWatchList();
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
