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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                tvAmount.setText(String.format(context.getString(R.string.style_amount), dataList.get(pendingHolder.getAdapterPosition()).getAmount()));

                btnAppoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String cName = String.valueOf(spCollector.getSelectedItem());
                        final String cEmail = cName.substring(cName.indexOf("(") + 1, cName.indexOf(")"));
                        String phone = tvPhone.getText().toString();
                        phone = phone.substring(phone.lastIndexOf(":") + 2);
                        //appointCollector();
                        CollectionReference ref = FirebaseFirestore.getInstance().collection("loans");
                        Map<String, Object> map = new HashMap<>();
                        map.put("appoint", cEmail);
                      /*  ref.document(phone).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Collector appointed: " + cEmail, Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            }
                        });*/
                        ref.document(phone).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Collector appointed: " + cEmail, Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        });
                        Loan loan = new Loan(cEmail);
                        ref.document(phone).set(loan, SetOptions.mergeFields("appoint"));


                    }
                });
            }
        });

       /* new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);

                final String phone = pendingHolder.tvPhone.getText().toString();

                PopupMenu menu = new PopupMenu(context, v);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.menu_machine, menu.getMenu());
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteMach:
                                callback.removeItem(phone);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                return true;
            }
        };*/

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

    private void addCollectors(final ArrayAdapter adapter) {
        final CustomProgressDialog dialog = new CustomProgressDialog(context);
        CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
        dialog.showDialog(Constants.LOADING);
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Collector> list = queryDocumentSnapshots.toObjects(Collector.class);
                for (Collector collector : list) {
                    adapter.add(collector.getName() + " (" + collector.getEmail() + ")");
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