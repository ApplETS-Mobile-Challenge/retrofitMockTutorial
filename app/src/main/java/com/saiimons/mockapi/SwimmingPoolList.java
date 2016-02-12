package com.saiimons.mockapi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by saiimons on 16-02-11.
 */
public class SwimmingPoolList {
    @SerializedName("features")
    public ArrayList<SwimmingPool> swimmingPools = new ArrayList<>();
}
