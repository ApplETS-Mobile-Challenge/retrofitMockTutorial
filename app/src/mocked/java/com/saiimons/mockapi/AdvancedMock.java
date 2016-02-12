package com.saiimons.mockapi;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import rx.Observable;

/**
 * Created by saiimons on 16-02-11.
 */
public class AdvancedMock implements SwimmingPoolsApi {
    private final Context context;
    private final BehaviorDelegate<SwimmingPoolsApi> delegate;
    private final Retrofit retrofit;

    public AdvancedMock(Context context, BehaviorDelegate<SwimmingPoolsApi> delegate, Retrofit retrofit) {
        this.context = context;
        this.delegate = delegate;
        this.retrofit = retrofit;
    }


    @Override
    public Observable<SwimmingPoolList> getSwimmingPools() {
        SwimmingPoolList list;
        try {
            Converter<ResponseBody, SwimmingPoolList> converter = retrofit.responseBodyConverter(SwimmingPoolList.class, SwimmingPoolsApi.class.getMethod("getSwimmingPools").getAnnotations());
            String contents = readContentsFromAsset(context, "mocks/getSwimmingPools.json");
            ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), contents);
            list = converter.convert(body);
        } catch (IOException e) {
            return delegate.returning(Calls.failure(e)).getSwimmingPools();
        } catch (NoSuchMethodException e) {
            return delegate.returning(Calls.failure(new IOException(e))).getSwimmingPools();
        }
        return delegate.returningResponse(list).getSwimmingPools();
    }

    private String readContentsFromAsset(Context context, String filename) throws IOException {
        InputStream inputStream = context.getAssets().open(filename);
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }
}
