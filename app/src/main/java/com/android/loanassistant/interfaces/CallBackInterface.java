package com.android.loanassistant.interfaces;

import com.android.loanassistant.model.Collector;

public interface CallBackInterface {

    public void onNextFragment(Collector model);

    public void onPreviousFragment(String address);

    public void onNextFragRecord(Collector model);

    public void onPreviousFragRecord(String address);
}
