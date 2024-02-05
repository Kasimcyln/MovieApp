package com.kceylan.app.repositories;

import com.kceylan.app.network.ApiClient;
import com.kceylan.app.network.ApiService;
import com.kceylan.app.responses.TVShowsResponse;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class SearchTVShowRepository {
    private ApiService apiService;


    public SearchTVShowRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public Maybe<TVShowsResponse> searchTVShow(String query, int page) {
       return apiService.searchTVShow(query,page)
               .onErrorResumeNext(throwable -> {
                   throwable.printStackTrace();
                   return Maybe.error(throwable);
               });
    }
}
