package com.android.loanassistant.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.AdapterCallback;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.holder.PendingHolder;
import com.android.loanassistant.model.Collector;
import com.android.loanassistant.model.Loan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingHolder> {
    private List<Loan> dataList;
    private Context context;
    private AdapterCallback callback;
    private Dialog mDialog;

    public PendingAdapter(Context context, List<Loan> dataList, AdapterCallback callback) {
        this.dataList = dataList;
        this.context = context;
        this.callback = callback;
    }

    public PendingAdapter(Context context, List<Loan> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public PendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_pending, parent, false);
        final PendingHolder pendingHolder = new PendingHolder(v);

        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_pending);

        pendingHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item click", Toast.LENGTH_SHORT).show();
                mDialog.show();
                final TextView tvPhone = mDialog.findViewById(R.id.dialogPhone);
                TextView tvAmount = mDialog.findViewById(R.id.dialogAmount);
                final Spinner spCollector = mDialog.findViewById(R.id.dialogCollector);
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item);
                addCollectors(adapter);
                adapter.add("---Select Employee Name---");
                spCollector.setAdapter(adapter);
                spCollector.setPrompt("---Select Employee Name---");
                Button btnAppoint = mDialog.findViewById(R.id.btnAppoint);
                tvPhone.setText(String.format(context.getString(R.string.style_phone), dataList.get(pendingHolder.getAdapterPosition()).getPhone()));
                tvAmount.append(dataList.get(pendingHolder.getAdapterPosition()).getAmount());

                btnAppoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String cName = String.valueOf(spCollector.getSelectedItem());
                        //appointCollector();
                        DocumentReference ref = FirebaseFirestore.getInstance().collection("loans").document(tvPhone.getText().toString());
                        ref.update("appoint", cName).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Collector appointed: " + cName, Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            }
                        });


                    }
                });

            }
        });

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

      /*  pendingHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingHolder.tvInterest.setVisibility(View.VISIBLE);
                pendingHolder.tvTime.setVisibility(View.VISIBLE);
                pendingHolder.tvInitDate.setVisibility(View.VISIBLE);
                pendingHolder.tvDueDate.setVisibility(View.VISIBLE);
                pendingHolder.tvInstallments.setVisibility(View.VISIBLE);
            }
        });*/

        return pendingHolder;
    }

    private void appointCollector() {
    }

    private void addCollectors(final ArrayAdapter adapter) {
        final CustomProgressDialog dialog = new CustomProgressDialog(context);
        CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
        dialog.showDialog(Constants.LOADING);
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Collector> list = queryDocumentSnapshots.toObjects(Collector.class);
                for (Collector collector : list) {
                    adapter.add(collector.getName());
                }
                dialog.stopDialog();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull PendingHolder holder, int position) {
        holder.tvPhone.setText(String.format(context.getString(R.string.style_phone), dataList.get(position).getPhone()));
        holder.tvAmount.setText(String.format(context.getString(R.string.style_amount), dataList.get(position).getAmount()));
        holder.tvInterest.setText(String.format(context.getString(R.string.style_interest), dataList.get(position).getInterest()));
        holder.tvTime.setText(String.format(context.getString(R.string.style_time), dataList.get(position).getTime()));
        holder.tvInitDate.setText(String.format(context.getString(R.string.style_init_date), dataList.get(position).getStartDate()));
        holder.tvDueDate.setText(String.format(context.getString(R.string.style_due_date), dataList.get(position).getDueDate()));
//        holder.tvInstallments.setText(String.format(context.getString(R.string.style_installments), String.valueOf(dataList.get(position).getDates())));
        holder.tvInstallments.setText(String.format(context.getString(R.string.style_installments), dataList.get(position).getDates().toString()));
//        Log.i("pendingHolder",holder.toString());
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

}