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
import com.android.loanassistant.model.Collector;

import java.util.List;

public class CollectorAdapter extends RecyclerView.Adapter<CollectorHolder> {
    private List<Collector> dataList;
    private Context context;
    private AdapterCallback callback;

    public CollectorAdapter(Context context, List<Collector> dataList, AdapterCallback callback) {
        this.dataList = dataList;
        this.context = context;
        this.callback = callback;
    }

    public CollectorAdapter(Context context, List<Collector> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CollectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_collector, parent, false);
        final CollectorHolder collectorHolder = new CollectorHolder(v);

/*
        collectorHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
        });
*/
        return collectorHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectorHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getName());
        holder.tvPhone.setText(dataList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

}