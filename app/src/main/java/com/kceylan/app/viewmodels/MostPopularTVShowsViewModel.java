package com.kceylan.app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kceylan.app.repositories.MostPopularTVShowsRepository;
import com.kceylan.app.responses.TVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MostPopularTVShowsViewModel extends ViewModel {

    public MutableLiveData<TVShowsResponse> data = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel (){
        mostPopularTVShowsRepository = new MostPopularTVShowsRepository();

    }


    public void getMostPopularTVShows(int page) {
        compositeDisposable.add(
        mostPopularTVShowsRepository.getMostPopularTVShows(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {

                })
                .subscribe(tvShowsResponse -> data.setValue(tvShowsResponse))
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
