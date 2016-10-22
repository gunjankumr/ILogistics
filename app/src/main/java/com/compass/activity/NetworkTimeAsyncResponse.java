package com.compass.activity;

public interface NetworkTimeAsyncResponse {
    void onNetworkDateTimeObtained(String networkDateTime, CheckInOutType type);
}