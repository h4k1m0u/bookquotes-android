package me.bookquotes.quotes;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Retrofit API interface to register a new user.
 */

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("auth/register/")
    Call<User> register(@Field("username") String username, @Field("password") String password);
}
