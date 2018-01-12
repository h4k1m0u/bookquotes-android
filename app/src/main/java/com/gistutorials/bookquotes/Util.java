package com.gistutorials.bookquotes;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Util {
    static public Retrofit getBuilder() {
        // logging interceptor to print requests (change to BODY for more detail)
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        // initialize the json parser with retrofit
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://gistutorials.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return builder;
    }
}