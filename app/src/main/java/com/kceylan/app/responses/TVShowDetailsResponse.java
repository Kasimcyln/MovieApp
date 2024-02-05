package com.kceylan.app.responses;

import com.google.gson.annotations.SerializedName;
import com.kceylan.app.models.TVShowDetails;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }


}
