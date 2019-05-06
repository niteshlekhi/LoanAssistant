package com.android.loanassistant.interfaces;

import com.android.loanassistant.model.Appointed;
import com.android.loanassistant.model.Collector;
import com.android.loanassistant.model.Loan;

public interface CollectorInterface {

    public void showUserDetails(Appointed model);

    public void showMap(String address);

    /*public void onNextFragRecord(Loan loan);*/

//    public void onPreviousFragRecord(Loan loan);
}
