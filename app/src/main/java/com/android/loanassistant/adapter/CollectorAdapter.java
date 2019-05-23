package com.android.loanassistant.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.AdapterCallback;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.holder.CollectorHolder;
import com.android.loanassistant.model.Collector;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CollectorAdapter extends RecyclerView.Adapter<CollectorHolder> {
    private List<Collector> dataList;
    private Context context;
    private Dialog mDialog;
    private AdapterCallback callback;
    private ImageView imgCollector;
    private int position;
    private static CollectorAdapter.MyClickListener sClickListener;

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
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_collector);
        collectorHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Item click", Toast.LENGTH_SHORT).show();

                final CustomProgressDialog dialog = new CustomProgressDialog(context);
                dialog.showDialog(Constants.LOADING);
                imgCollector = mDialog.findViewById(R.id.imgCollector);
                TextView tvName = mDialog.findViewById(R.id.dialog3Name);
                TextView tvPhone = mDialog.findViewById(R.id.dialog3Phone);
                final TextView tvAadhar = mDialog.findViewById(R.id.dialog3Aadhar);
                TextView tvEmail = mDialog.findViewById(R.id.dialog3Email);
                final TextView tvAddress = mDialog.findViewById(R.id.dialog3Address);
                Button btnDelete = mDialog.findViewById(R.id.btnDelete);

                tvName.setText(String.format(context.getString(R.string.style_name), dataList.get(collectorHolder.getAdapterPosition()).getName()));
                tvEmail.setText(String.format(context.getString(R.string.style_email), dataList.get(collectorHolder.getAdapterPosition()).getEmail()));
                tvPhone.setText(String.format(context.getString(R.string.style_phone), dataList.get(collectorHolder.getAdapterPosition()).getPhone()));
                final String email = dataList.get(collectorHolder.getAdapterPosition()).getEmail();

                CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
                ref.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Collector collector = documentSnapshot.toObject(Collector.class);
                        getImageFromStorage(collector.getDpUrl());
                        tvAadhar.setText(String.format(context.getString(R.string.style_aadhar), collector.getAadhar()));
                        tvAddress.setText(String.format(context.getString(R.string.style_address), collector.getAddress()));
                    }
                });
                dialog.stopDialog();
                mDialog.show();

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position=collectorHolder.getAdapterPosition();
                        final AlertDialog alertDialog = new android.app.AlertDialog.Builder(context)
                                .setMessage(R.string.delete_item)
                                .setIcon(context.getDrawable(R.mipmap.ic_launcher))
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog1, int which) {
                                        removeItem(email);
                                        dialog1.dismiss();
                                        mDialog.dismiss();
                                    }
                                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog1, int which) {
                                        dialog1.dismiss();
                                    }
                                }).show();
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
        return collectorHolder;
    }

    private void removeItem(String email) {
        CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
        ref.document(email).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Record Deleted!", Toast.LENGTH_SHORT).show();
                dataList.remove(position);
                notifyItemRemoved(position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Record not Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

  /*  @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }*/

    private void getImageFromStorage(String dpUrl) {
//        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(dpUrl);
        Glide.with(context)
                .load(dpUrl) // Uri of the picture
                .centerCrop()
                .into(imgCollector);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectorHolder holder, int position) {
        holder.tvName.setText(String.format(context.getString(R.string.style_name), dataList.get(position).getName()));
        holder.tvPhone.setText(String.format(context.getString(R.string.style_phone), dataList.get(position).getPhone()));
        holder.tvEmail.setText(String.format(context.getString(R.string.style_email), dataList.get(position).getEmail()));
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    interface MyClickListener {
        void onItemClick(int position, View v);
    }

}