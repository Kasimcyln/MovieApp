package com.kceylan.app.responses;

import com.google.gson.annotations.SerializedName;
import com.kceylan.app.models.TVShow;

import java.util.List;

public class TVShowsResponse {

    @SerializedName("page")
    private int id;

    @SerializedName("total")
    private int total;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("tv_shows")
    private List<TVShow> tvShows;

    public int getId() {
        return id;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }
}
