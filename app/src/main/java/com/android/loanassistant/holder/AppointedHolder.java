package com.android.loanassistant.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.loanassistant.R;

public class AppointedHolder extends RecyclerView.ViewHolder {
    public TextView tvName,tvPhone, tvAmount, tvInitDate, tvDueDate, tvInstallments;

    public AppointedHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.lstAName);
        tvPhone = itemView.findViewById(R.id.lstAPhone);
        tvAmount = itemView.findViewById(R.id.lstAAmount);
        /*tvTime = itemView.findViewById(R.id.lstTime);
        tvInitDate = itemView.findViewById(R.id.lstInitialDate);
        tvDueDate = itemView.findViewById(R.id.lstDueDate);
        tvInstallments = itemView.findViewById(R.id.lstInstallments);*/
    }
}