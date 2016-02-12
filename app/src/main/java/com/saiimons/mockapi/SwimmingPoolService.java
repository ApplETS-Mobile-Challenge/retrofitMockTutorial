package com.saiimons.mockapi;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saiimons on 16-02-11.
 */
public class SwimmingPoolService {
    private final SwimmingPoolServiceDelegate delegate;

    public SwimmingPoolService(Context context) {
        delegate = new SwimmingPoolServiceImpl(context);
    }

    public SwimmingPoolsApi getApi() {
        return delegate.getApi();
    }

    public static class SwimmingPoolServiceDelegate {

        protected final Context context;

        protected SwimmingPoolServiceDelegate(Context context) {
            this.context = context;
        }

        protected Retrofit.Builder getRetrofitBuilder() {
            Gson gson = new GsonBuilder().registerTypeAdapter(SwimmingPool.class, new SwimmingPool.Parser()).create();
            return new Retrofit.Builder()
                    .baseUrl("http://donnees.ville.montreal.qc.ca/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson));
        }

        protected Retrofit getRetrofit() {
            return getRetrofitBuilder().build();
        }

        public SwimmingPoolsApi getApi() {
            return getRetrofit().create(SwimmingPoolsApi.class);
        }
    }
}
