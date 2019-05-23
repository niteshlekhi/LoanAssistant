package com.android.loanassistant.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.loanassistant.R;
import com.android.loanassistant.adapter.CollectorAdapter;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.model.Collector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectorDetails extends Fragment {

    @BindView(R.id.rvCollector)
    RecyclerView recyclerView;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private CustomProgressDialog dialog;
    private int count = 0;
    private CollectorAdapter collectorAdapter;
    private List<Collector> list;

    public CollectorDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collector_details, container, false);
        ButterKnife.bind(this, view);

        initViews(view);
        dialog.showDialog(Constants.LOADING);
        updateData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();

    }

    private void initViews(View view) {
        getActivity().setTitle(Constants.COLLECTOR_DETAILS);
        dialog = new CustomProgressDialog(getActivity());
        db = FirebaseFirestore.getInstance();
        ref = db.collection("collector");
        dialog.showDialog(Constants.LOADING);
        recyclerView = view.findViewById(R.id.rvCollector);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void updateData() {
        if (list != null)
            list.clear();
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list = queryDocumentSnapshots.toObjects(Collector.class);
                count = list.size();
                collectorAdapter = new CollectorAdapter(getContext(), list);
                recyclerView.setAdapter(collectorAdapter);
            }
        });
        dialog.stopDialog();
    }

}
