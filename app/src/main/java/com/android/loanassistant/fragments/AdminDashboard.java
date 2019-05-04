package com.android.loanassistant.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.loanassistant.R;
import com.android.loanassistant.adapter.PendingAdapter;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.model.Loan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminDashboard extends Fragment {
    @BindView(R.id.rvPending)
    RecyclerView recyclerView;
    @BindView(R.id.totalCount)
    TextView txtEntries;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private CustomProgressDialog dialog;
    private int count = 0;
    private PendingAdapter pendingAdapter;

    public AdminDashboard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(Constants.DASHBOARD);
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
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
        ref = db.collection("loans");
        dialog.showDialog(Constants.LOADING);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateData() {
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Loan> list = queryDocumentSnapshots.toObjects(Loan.class);
                Log.i("loanList", list.toString());
                count = list.size();
                txtEntries.append(String.valueOf(count));
                pendingAdapter = new PendingAdapter(getContext(), list);
                recyclerView.setAdapter(pendingAdapter);
            }
        });
        dialog.stopDialog();
    }
}