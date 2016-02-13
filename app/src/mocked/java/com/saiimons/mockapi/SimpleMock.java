package com.saiimons.mockapi;

import retrofit2.mock.BehaviorDelegate;
import rx.Observable;

/**
 * Create a simple mock with pre-defined POJOs
 */
public class SimpleMock implements SwimmingPoolsApi {
    private final BehaviorDelegate<SwimmingPoolsApi> delegate;

    public SimpleMock(BehaviorDelegate<SwimmingPoolsApi> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Observable<SwimmingPoolList> getSwimmingPools() {
        SwimmingPoolList list = new SwimmingPoolList();
        list.swimmingPools.add(new SwimmingPool("Piscine de champagne", "Chez Parée"));
        list.swimmingPools.add(new SwimmingPool("Parc Olympique", "4141 Avenue Pierre-de Coubertin, Montréal, QC H1V 3N7"));
        return delegate.returningResponse(list).getSwimmingPools();
    }
}
