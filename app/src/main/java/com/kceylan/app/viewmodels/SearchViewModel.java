package com.kceylan.app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kceylan.app.repositories.SearchTVShowRepository;
import com.kceylan.app.responses.TVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private final SearchTVShowRepository searchTVShowRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<TVShowsResponse> tvShowsLiveData = new MutableLiveData<>();

    public SearchViewModel() {
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public void searchTVShow(String query, int page) {
        compositeDisposable.add(
                searchTVShowRepository.searchTVShow(query,page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShowsResponse -> tvShowsLiveData.setValue(tvShowsResponse), throwable -> {

                        })
        );
    }

}
