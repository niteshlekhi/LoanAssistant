package com.android.loanassistant.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.loanassistant.R;

public class CustomerHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvPhone,tvAadhar,tvAddress;

    public CustomerHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.lstCustName);
        tvPhone = itemView.findViewById(R.id.lstPhone);
        tvAadhar=itemView.findViewById(R.id.lstAadhar);
        tvAddress=itemView.findViewById(R.id.lstAddress);
    }
}
