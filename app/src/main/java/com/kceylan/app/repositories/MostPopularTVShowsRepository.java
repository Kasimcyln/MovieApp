package com.kceylan.app.repositories;

import com.kceylan.app.network.ApiClient;
import com.kceylan.app.network.ApiService;
import com.kceylan.app.responses.TVShowsResponse;

import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.core.Maybe;

public class MostPopularTVShowsRepository {

    private ApiService apiService;

    public MostPopularTVShowsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    public Maybe<TVShowsResponse> getMostPopularTVShows(int page) {
        return apiService.getMostPopularTVShows(page)
                .doOnError(throwable -> {})
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof SocketTimeoutException) {
                        return Maybe.error(throwable);
                    } else {
                        throwable.printStackTrace();
                    }
                    return Maybe.error(throwable);
                }).retry(10);
    }

}
