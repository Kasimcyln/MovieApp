package com.kceylan.app.repositories;

import com.kceylan.app.network.ApiClient;
import com.kceylan.app.network.ApiService;
import com.kceylan.app.responses.TVShowDetailsResponse;

import io.reactivex.rxjava3.core.Maybe;

public class TVShowDetailsRepository {

    ApiService apiService;

    public TVShowDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public Maybe<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return apiService.getTVShowDetails(tvShowId)
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    return Maybe.error(throwable);
                });
    }
}
