package com.android.loanassistant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.loanassistant.R;
import com.android.loanassistant.adapter.CustomerAdapter;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDetails extends Fragment {

    @BindView(R.id.rvCustomer)
    RecyclerView recyclerView;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private CustomProgressDialog dialog;
    private int count = 0;
    private CustomerAdapter customerAdapter;

    public CustomerDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);

        ButterKnife.bind(this, view);

        initViews(view);
        dialog.showDialog(Constants.LOADING);
        updateData();

        return view;
    }

    private void initViews(View view) {
        getActivity().setTitle(Constants.CUSTOMER_DETAILS);
        dialog = new CustomProgressDialog(getActivity());
        db = FirebaseFirestore.getInstance();
        ref = db.collection("user");
        dialog.showDialog(Constants.LOADING);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateData() {
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> list = queryDocumentSnapshots.toObjects(User.class);
                count = list.size();
                customerAdapter = new CustomerAdapter(getContext(), list);
                recyclerView.setAdapter(customerAdapter);
            }
        });
        dialog.stopDialog();
    }
}