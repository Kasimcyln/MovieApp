package com.kceylan.app.listeners;

import com.kceylan.app.models.TVShow;

public interface FavoriteListener {
    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchList(TVShow tvShow, int position);
}
