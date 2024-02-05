package com.kceylan.app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kceylan.app.database.TVShowsDatabase;
import com.kceylan.app.models.TVShow;
import com.kceylan.app.repositories.TVShowDetailsRepository;
import com.kceylan.app.responses.TVShowDetailsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private final TVShowDetailsRepository tvShowDetailsRepository;
    private final TVShowsDatabase tvShowsDatabase;
    public MutableLiveData<TVShowDetailsResponse> tvShowDetailsLiveData = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public void getTVShowDetails(String tvShowId) {
        compositeDisposable.add(
                tvShowDetailsRepository.getTVShowDetails(tvShowId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShowDetailsResponse -> tvShowDetailsLiveData.setValue(tvShowDetailsResponse), throwable -> {

                        })
        );
    }

    public Completable addToWatchList(TVShow tvShow) {
        if (tvShowsDatabase != null) {
            return tvShowsDatabase.tvShowDao().addWatchList(tvShow)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return Completable.error(new Throwable("TVShowDao is null"));
        }
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId) {
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);

    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().removeFromWatchList(tvShow);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
