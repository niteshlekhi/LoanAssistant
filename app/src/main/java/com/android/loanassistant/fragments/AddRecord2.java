package com.android.loanassistant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.interfaces.CallBackInterface;
import com.android.loanassistant.model.Loan;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecord2 extends Fragment {

    @BindView(R.id.reviewPhone)
    TextView txtPhone;

    @BindView(R.id.reviewAmount)
    TextView txtAmount;

    @BindView(R.id.reviewRate)
    TextView txtRate;

    @BindView(R.id.reviewDeducted)
    TextView txtDeducted;

    @BindView(R.id.reviewDueDate)
    TextView txtDueDate;

    @BindView(R.id.reviewInterest)
    TextView txtInterest;

    @BindView(R.id.reviewStartDate)
    TextView txtStartDate;

    @BindView(R.id.reviewTime)
    TextView txtTime;

    @BindView(R.id.btnDone)
    Button btnDone;

    private CustomProgressDialog dialog;
    private char duration = 'w';
    private String startDate;
    private String dueDate;
    private CallBackInterface callBackInterface;

    public AddRecord2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_record2, container, false);
        getActivity().setTitle(Constants.ADD_RECORD);

        ButterKnife.bind(this, view);
        dialog = new CustomProgressDialog(getActivity());

        Bundle bundle = new Bundle();
        if (getArguments() != null)
            bundle = getArguments();
        DecimalFormat format=new DecimalFormat("0.00");

        Loan loan = (Loan) bundle.getSerializable("objSave2");
        txtPhone.append(loan.getPhone());
        txtRate.append(loan.getRate());
        txtInterest.append(loan.getInterest());
        txtAmount.append(loan.getAmount());
        txtTime.append(String.valueOf(loan.getTime()));
        txtStartDate.append(loan.getStartDate());
        txtDueDate.append(loan.getDueDate());
        txtDeducted.append(loan.getInterest());

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        return view;
    }

    public void clearData() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new AddRecord());
        ft.commit();
    }

}