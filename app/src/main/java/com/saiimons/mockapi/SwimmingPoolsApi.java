package com.saiimons.mockapi;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by saiimons on 16-02-11.
 */
public interface SwimmingPoolsApi {
    @GET("/dataset/4604afb7-a7c4-4626-a3ca-e136158133f2/resource/e202c0f4-d65d-4d5f-893d-dc392d83298d/download/piscines1.json")
    Observable<SwimmingPoolList> getSwimmingPools();
}
