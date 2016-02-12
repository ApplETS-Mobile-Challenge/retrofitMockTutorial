package com.saiimons.mockapi;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by saiimons on 16-02-11.
 */
public class SwimmingPoolServiceImpl extends SwimmingPoolService.SwimmingPoolServiceDelegate {
    public SwimmingPoolServiceImpl(Context context) {
        super(context);
    }

    @Override
    protected Retrofit getRetrofit() {
        // Create a log interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Create a client with the interceptor
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Build the client
        return getRetrofitBuilder().client(client).build();
    }

}
