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
import com.android.loanassistant.adapter.AppointedAdapter;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.interfaces.CollectorInterface;
import com.android.loanassistant.model.Appointed;
import com.android.loanassistant.model.Loan;
import com.android.loanassistant.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class C_Dashboard extends Fragment {

    @BindView(R.id.rvAppointed)
    RecyclerView recyclerView;
    @BindView(R.id.totalCount)
    TextView txtEntries;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private CustomProgressDialog dialog;
    private int count = 0;
    private AppointedAdapter appointedAdapter;
    private List<Loan> loanList;
    private CollectionReference uRef;
    private List<Appointed> list;
    private List<User> userList;
    private CollectorInterface collectorInterface;

    public C_Dashboard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_dashboard, container, false);
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
        uRef = db.collection("user");
        dialog.showDialog(Constants.LOADING);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setCallBack(CollectorInterface collectorInterface) {
        this.collectorInterface = collectorInterface;
    }

    private void updateData() {
        if (list != null)
            list.clear();
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            ref.whereEqualTo("appoint", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    loanList = queryDocumentSnapshots.toObjects(Loan.class);
                    list = new ArrayList<>(loanList.size());

                    int index = 0;
                    for (Loan loan : loanList) {
                        /*String name = getUserName(loan.getPhone());
                        Appointed model = new Appointed(name, loan.getPhone(), loan.getAmount());*/
                        Appointed model = new Appointed(loan.getPhone(), loan.getAmount());
                        list.add(index, model);
                        ++index;
                    }

//                getUsers(loanList);


                    Log.i("loanList", list.toString());
                    count = list.size();
                    txtEntries.setText(String.valueOf(count));
                    appointedAdapter = new AppointedAdapter(getContext(), list, collectorInterface);
                    recyclerView.setAdapter(appointedAdapter);
                }
            });
        }
        dialog.stopDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
            updateData();

    }

    /*private String getUserName(String phone) {
        final String[] name = new String[1];
        uRef.whereEqualTo("phone",phone).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                User user=queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                name[0] =user.getName();
            }
        });

        return name[0];
    }


    private void getUsers(List<Loan> loanList) {

        uRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userList = queryDocumentSnapshots.toObjects(User.class);

                for(User user:userList){
                    int index=0;
                    if (list.containsAll(Collections.singleton(user.getPhone()))){
                        index=list.indexOf(Collections.singleton(user.getPhone()));
                        list.add(index);
//                        ++index;
                    }
                    *//*if(index>=list.size())
                        break;*//*
                }


            }
        });
    }*/
}