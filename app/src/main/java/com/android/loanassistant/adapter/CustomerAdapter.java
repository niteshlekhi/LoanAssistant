package com.android.loanassistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.AdapterCallback;
import com.android.loanassistant.holder.CollectorHolder;
import com.android.loanassistant.holder.CustomerHolder;
import com.android.loanassistant.model.Collector;
import com.android.loanassistant.model.User;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerHolder> {
    private List<User> dataList;
    private Context context;
    private AdapterCallback callback;

    public CustomerAdapter(Context context, List<User> dataList, AdapterCallback callback) {
        this.dataList = dataList;
        this.context = context;
        this.callback = callback;
    }

    public CustomerAdapter(Context context, List<User> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_customer, parent, false);
        final CustomerHolder customerHolder = new CustomerHolder(v);

        /*new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);

                final String mobile = mobileHolder.tvMobile.getText().toString();

                PopupMenu menu = new PopupMenu(context, v);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.menu_machine, menu.getMenu());
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteMach:
                                callback.removeItem(mobile);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                return true;
            }
        });*/
        return customerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getName());
        holder.tvPhone.setText(dataList.get(position).getPhone());
        holder.tvAadhar.setText(dataList.get(position).getAadhar());
        holder.tvAddress.setText(dataList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

}