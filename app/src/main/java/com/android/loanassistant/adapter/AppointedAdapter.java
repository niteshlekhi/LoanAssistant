package com.android.loanassistant.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.loanassistant.PaymentActivity;
import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.holder.AppointedHolder;
import com.android.loanassistant.interfaces.CollectorInterface;
import com.android.loanassistant.model.Appointed;
import com.android.loanassistant.model.Collector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AppointedAdapter extends RecyclerView.Adapter<AppointedHolder> {
    private List<Appointed> dataList;
    private Context context;
    private CollectorInterface callback;
    private Dialog mDialog;

    public AppointedAdapter(Context context, List<Appointed> dataList, CollectorInterface callback) {
        this.dataList = dataList;
        this.context = context;
        this.callback = callback;
    }

    public AppointedAdapter(Context context, List<Appointed> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_appointed, parent, false);
        final AppointedHolder pendingHolder = new AppointedHolder(v);

        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_pending);

        pendingHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Appointed model = new Appointed(dataList.get(pendingHolder.getAdapterPosition()).getPhone(), dataList.get(pendingHolder.getAdapterPosition()).getAmount());
                /*if (callback != null)
                    callback.showUserDetails(model);*/

                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("keyPhone", dataList.get(pendingHolder.getAdapterPosition()).getPhone());
                intent.putExtra("keyAmount", dataList.get(pendingHolder.getAdapterPosition()).getAmount());
                context.startActivity(intent);

/*                Bundle bundle = new Bundle();
                bundle.putSerializable("objAppointed", model);
                FragmentManager manager = ((CollectorPanel) context).getSupportFragmentManager();
                UserPayment payment = new UserPayment();
                payment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction().add(R.id.frameLayout1, payment);
                transaction.commit();*/

                /*

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
                        String phone = tvPhone.getText().toString();
                        //appointCollector();
                        CollectionReference ref = FirebaseFirestore.getInstance().collection("loans");
                        Map<String, Object> map = new HashMap<>();
                        map.put("appoint", cName);
                        ref.document(phone).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
*/
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
    public void onBindViewHolder(@NonNull AppointedHolder holder, int position) {
        holder.tvName.setText(String.format(context.getString(R.string.style_name), dataList.get(position).getName()));
        holder.tvPhone.setText(String.format(context.getString(R.string.style_phone), dataList.get(position).getPhone()));
        holder.tvAmount.setText(String.format(context.getString(R.string.style_amount), dataList.get(position).getAmount()));
//        Log.i("pendingHolder",holder.toString());
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

}