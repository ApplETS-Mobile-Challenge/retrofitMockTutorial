package com.saiimons.mockapi;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;

/**
 * Created by saiimons on 16-02-11.
 */
public class AlmostJakeWhartonMock {
    public static <T> T create(Context context, BehaviorDelegate<SwimmingPoolsApi> delegate, Retrofit retrofit, Class<T> service) {
        ClassLoader loader = service.getClassLoader();
        Class<?>[] interfaces = new Class<?>[]{service};
        InvocationHandler handler = new Handler(context, delegate, retrofit);
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    private static class Handler implements InvocationHandler {
        private final Context context;
        private final BehaviorDelegate<SwimmingPoolsApi> delegate;
        private final Retrofit retrofit;

        private Handler(Context context, BehaviorDelegate<SwimmingPoolsApi> delegate, Retrofit retrofit) {
            this.context = context;
            this.delegate = delegate;
            this.retrofit = retrofit;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {// If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            try {
                Converter<ResponseBody, ?> converter = retrofit.responseBodyConverter(
                        ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0],
                        method.getAnnotations()
                );
                String contents = readContentsFromAsset(context, "mocks/" + method.getName() + ".json");
                ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), contents);
                Object obj = converter.convert(body);
                return method.invoke(delegate.returningResponse(obj), args);
            } catch (IOException e) {
                return delegate.returning(Calls.failure(e)).getSwimmingPools();
            }
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

}
