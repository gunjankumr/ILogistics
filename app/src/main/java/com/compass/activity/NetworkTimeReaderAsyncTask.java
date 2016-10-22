package com.compass.activity;


import com.google.common.base.Preconditions;

import com.compass.utils.Utilities;

import android.os.AsyncTask;

public class NetworkTimeReaderAsyncTask extends AsyncTask {
    private NetworkTimeAsyncResponse delegate = null;
    private String networkDateTime;
    private CheckInOutType checkInOutType;

    public NetworkTimeReaderAsyncTask(NetworkTimeAsyncResponse delegate) {
        Preconditions.checkNotNull(delegate);
        this.delegate = delegate;
    }

    public void setCheckInOutType(CheckInOutType type) {
        checkInOutType = type;
    }

    @Override protected Object doInBackground(Object[] params) {
        networkDateTime = String.valueOf(Utilities.getCurrentNetworkTime());
        return null;
    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (delegate == null) {
            throw new RuntimeException("delegate is null");
        }
        delegate.onNetworkDateTimeObtained(networkDateTime, checkInOutType);
    }
}
