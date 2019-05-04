package com.android.loanassistant.interfaces;

import com.android.loanassistant.model.Collector;
import com.android.loanassistant.model.Loan;
import com.android.loanassistant.model.User;

public interface CallBackInterface {

    public void onNextFragment(Collector model);

    public void onPreviousFragment(String address);

    public void onNextFragRecord(Loan loan);

//    public void onPreviousFragRecord(Loan loan);
}
