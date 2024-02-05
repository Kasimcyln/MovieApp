package com.kceylan.app.network;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(
                    chain -> {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        return response;
                    }
            ).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.episodate.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}
