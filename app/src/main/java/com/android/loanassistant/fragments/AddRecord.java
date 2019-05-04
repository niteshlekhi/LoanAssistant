package com.android.loanassistant.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.helper.LoanCalculator;
import com.android.loanassistant.interfaces.CallBackInterface;
import com.android.loanassistant.model.Loan;
import com.android.loanassistant.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecord extends Fragment {

    @BindView(R.id.custName)
    EditText txtName;
    @BindView(R.id.custPhone)
    EditText txtPhone;
    @BindView(R.id.custAadhar)
    EditText txtAadhar;
    @BindView(R.id.custAddress)
    EditText txtAddress;

    @BindView(R.id.loanAmount)
    EditText txtAmount;

    @BindView(R.id.interestRate)
    EditText txtRate;

    @BindView(R.id.duration)
    EditText txtDuration;

    @BindView(R.id.spInstallments)
    Spinner spInstallments;

    @BindView(R.id.btnAddRecord)
    Button btnAddRecord;

    private CustomProgressDialog dialog;
    private char installment = 's';
    private String startDate;
    private String dueDate;
    private List<String> dates;
    private float interest;
    private CallBackInterface callBackInterface;

    public AddRecord() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_record, container, false);
        getActivity().setTitle(Constants.ADD_RECORD);
        ButterKnife.bind(this, view);
        ArrayAdapter spAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        spAdapter.add("Single");
        spAdapter.add("Monthly");
        spInstallments.setAdapter(spAdapter);
        spInstallments.setPrompt("Single");

        spInstallments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        installment = 's';
                        break;
                    case 1:
                        installment = 'm';
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog = new CustomProgressDialog(getActivity());

        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    dialog.showDialog(Constants.ADDING);
                    final float amount = Float.parseFloat(txtAmount.getText().toString());
                    final float rate = Float.parseFloat(txtRate.getText().toString());
                    final int time = Integer.parseInt(txtDuration.getText().toString());

                    new MyTask().execute(amount, rate, time);

                    final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                    //User data store
                    User user = new User(txtName.getText().toString(), txtPhone.getText().toString(), txtAadhar.getText().toString(), txtAddress.getText().toString());
                    final String phone = user.getPhone();
                    CollectionReference userRef = firestore.collection("user");
                    userRef.document(phone).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("userRef", "user Added");

                            //Loan data store
                            CollectionReference loanRef = firestore.collection("loans");
                            final Loan loan = new Loan(String.format("%.2f", amount), String.format("%.2f", rate), time, String.format("%.2f", interest), startDate, dueDate, phone, dates);
                            loanRef.document(phone).set(loan).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                            Log.i("userRef", "loan Added");
                                    Toast.makeText(getActivity(), "Loan Application Added!", Toast.LENGTH_SHORT).show();
                                    dialog.stopDialog();
                                    Log.i("LoanModel",loan.toString());

                                    callBackInterface.onNextFragRecord(loan);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Loan Application NOT Added!", Toast.LENGTH_SHORT).show();
                                    dialog.stopDialog();
//                            Log.i("loanRef", "loan not Added");
                                }
                            });
//                            callBackInterface.onNextFragRecord(loan);
//                            Toast.makeText(getActivity(), "User Added!", Toast.LENGTH_SHORT).show();
//                            dialog.stopDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Collector NOT Added!", Toast.LENGTH_SHORT).show();
                            dialog.stopDialog();
//                            Log.i("userRef", "user not Added");
                        }
                    });
                }
            }
        });
        return view;
    }

    public void setCallBack(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }

    private boolean validate() {
        boolean valid = true;
        /*String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();
        String aadhar = txtAadhar.getText().toString();
        String amount = txtAmount.getText().toString();
        String rate = txtRate.getText().toString();
        String duration = txtDuration.getText().toString();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || aadhar.isEmpty() || amount.isEmpty() || rate.isEmpty() || duration.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields ", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            //Name
            if (name.length() < 3) {
                txtName.setError("Invalid Name!");
                txtName.requestFocus();
                valid = false;
            } else {
                txtName.setError(null);
            }
            //Phone
            if (phone.length() < 10) {
                txtPhone.setError("Invalid Phone number");
                txtPhone.requestFocus();
                valid = false;
            } else {
                txtPhone.setError(null);
            }
            //Aadhar
            if (aadhar.length() < 12) {
                txtAadhar.setError("Invalid Aadhar");
                txtAadhar.requestFocus();
                valid = false;
            } else {
                txtAadhar.setError(null);
            }
        }*/

        return valid;
    }

    private class MyTask {

        public void execute(float amount, float rate, int time) {
            LoanCalculator calculator = new LoanCalculator(amount, rate, time);
            startDate = calculator.getCurrentDate();
            dueDate = calculator.getDueDate(startDate, time);

            Log.i("difference", String.valueOf(calculator.getDifferenceDays(startDate, dueDate)));
            interest = Float.parseFloat(String.format("%.2f", calculator.calculateInterest(amount, rate, time)));
            dates = calculator.installmentDates(startDate, dueDate, installment, time);
        }
    }

}
