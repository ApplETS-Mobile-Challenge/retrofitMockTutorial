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

    /**
     * Create the service which will provide the API.
     * The instantiated delegate contains the implementation.
     *
     * @param context as usual
     */
    public SwimmingPoolService(Context context) {
        delegate = new SwimmingPoolServiceImpl(context);
    }

    /**
     * Get the API implementation
     * @return the API implementation
     */
    public SwimmingPoolsApi getApi() {
        return delegate.getApi();
    }

    /**
     * Default Delegate class
     */
    public static class SwimmingPoolServiceDelegate {

        protected final Context context;

        protected SwimmingPoolServiceDelegate(Context context) {
            this.context = context;
        }

        /**
         * Create the default Retrofit Builder
         *
         * @return the default Retrofit Builder
         */
        protected Retrofit.Builder getRetrofitBuilder() {
            // The service has a specific parser
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            SwimmingPool.class,
                            new SwimmingPool.Parser()
                    )
                    .create();

            return new Retrofit.Builder()
                    .baseUrl("http://donnees.ville.montreal.qc.ca/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson));
        }

        /**
         * Create the Retrofit instance
         *
         * @return the Retrofit instance
         */
        protected Retrofit getRetrofit() {
            return getRetrofitBuilder().build();
        }

        /**
         * Create the SwimmingPoolsApi
         *
         * @return the SwimmingPoolsApi
         */
        public SwimmingPoolsApi getApi() {
            return getRetrofit().create(SwimmingPoolsApi.class);
        }
    }
}
