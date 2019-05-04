package com.android.loanassistant.interfaces;

import com.android.loanassistant.holder.PendingHolder;
import com.android.loanassistant.model.Loan;

public interface ItemClickListener {

    void onItemClicked(PendingHolder holder, Loan item, int pos);
}

/*
public interface GenericItemClickListener<T, VH extends PendingHolder> {

    void onItemClicked(VH vh, T item, int pos);
}*/
