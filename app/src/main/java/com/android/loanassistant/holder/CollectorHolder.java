package com.android.loanassistant.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.loanassistant.R;

public class CollectorHolder extends RecyclerView.ViewHolder {
    public TextView tvName, tvPhone,tvEmail;

    public CollectorHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.lstCtrName);
        tvPhone = itemView.findViewById(R.id.lstCtrPhone);
        tvEmail=itemView.findViewById(R.id.lstCtrEmail);
    }
}
