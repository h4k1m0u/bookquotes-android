package me.bookquotes.quotes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit API interface (to download quotes).
 */

public interface FeedAPI {
    @GET("api/quotes/?format=json")
    Call<Feed> getFeed(@Query("page") int page);
}
