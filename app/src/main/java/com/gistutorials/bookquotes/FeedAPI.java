package com.gistutorials.bookquotes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit API interface (to download quotes only for authenticated users).
 */

public interface FeedAPI {
    @GET("quotes/?format=json")
    Call<Feed> getFeed(@Query("page") int page);
}
