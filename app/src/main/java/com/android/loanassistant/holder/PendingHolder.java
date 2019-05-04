package com.android.loanassistant.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.loanassistant.R;

public class PendingHolder extends RecyclerView.ViewHolder {
    public TextView tvPhone, tvAmount, tvInterest, tvTime, tvInitDate, tvDueDate, tvInstallments;

    public PendingHolder(View itemView) {
        super(itemView);
        tvPhone = itemView.findViewById(R.id.lstPendingPhone);
        tvAmount = itemView.findViewById(R.id.lstAmount);
        tvInterest = itemView.findViewById(R.id.lstInterest);
        tvTime = itemView.findViewById(R.id.lstTime);
        tvInitDate = itemView.findViewById(R.id.lstInitialDate);
        tvDueDate = itemView.findViewById(R.id.lstDueDate);
        tvInstallments = itemView.findViewById(R.id.lstInstallments);
    }
}