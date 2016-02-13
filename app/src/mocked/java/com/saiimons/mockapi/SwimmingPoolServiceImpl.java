package com.saiimons.mockapi;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by saiimons on 16-02-11.
 */
public class SwimmingPoolServiceImpl extends SwimmingPoolService.SwimmingPoolServiceDelegate {
    public SwimmingPoolServiceImpl(Context context) {
        super(context);
    }

    @Override
    public SwimmingPoolsApi getApi() {
        // Get the Retrofit object
        Retrofit retrofit = getRetrofit();
        // Create a randomly behaving network
        NetworkBehavior behavior = NetworkBehavior.create();
        // Create a mocked Retrofit using these objects
        MockRetrofit mockRetrofit = new MockRetrofit
                .Builder(retrofit)
                .networkBehavior(behavior)
                .build();
        // Create the delegate implementing the API
        BehaviorDelegate<SwimmingPoolsApi> delegate = mockRetrofit.create(SwimmingPoolsApi.class);

        // Finally handle the mock
        // Simple Mock with Pojos
        //return new SimpleMock(delegate);





        // Mock the object byy handling json
        return new AdvancedMock(context, delegate, retrofit);





        // Mock the object using a proxy
        // return AlmostJakeWhartonMock.create(context, delegate, retrofit, SwimmingPoolsApi.class);
    }
}
