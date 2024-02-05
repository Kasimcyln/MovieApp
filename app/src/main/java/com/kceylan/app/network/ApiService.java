package com.kceylan.app.network;

import com.kceylan.app.responses.TVShowDetailsResponse;
import com.kceylan.app.responses.TVShowsResponse;


import io.reactivex.rxjava3.core.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("show-details")
    Maybe<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);

    @GET("most-popular")
    Maybe<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);

    @GET("search")
    Maybe<TVShowsResponse> searchTVShow(@Query("q") String query, @Query("page") int page);

}
