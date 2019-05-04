package com.android.loanassistant.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.interfaces.CallBackInterface;
import com.android.loanassistant.model.Collector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCollector extends Fragment {

    @BindView(R.id.ctrName)
    EditText txtName;

    @BindView(R.id.ctrEmail)
    EditText txtEmail;

    @BindView(R.id.ctrPhone)
    AutoCompleteTextView txtPhone;

    @BindView(R.id.ctrAadhar)
    EditText txtAadhar;

    @BindView(R.id.btnNextPage)
    Button btnNext;

    private CallBackInterface callBackInterface;

    public AddCollector() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_collector, container, false);
        getActivity().setTitle(Constants.ADD_COLLECTOR);
        ButterKnife.bind(this, view);

        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("objCollector") != null)) {
            Collector collector = (Collector) savedInstanceState
                    .getSerializable("objCollector");
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String name = txtName.getText().toString();
                    String email = txtEmail.getText().toString();
                    String phone = txtPhone.getText().toString();
                    String aadhar = txtAadhar.getText().toString();
                    Collector collector = new Collector(name, email, phone, aadhar);

                    if (callBackInterface != null)
                        callBackInterface.onNextFragment(collector);
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
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        String aadhar = txtAadhar.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || aadhar.isEmpty()) {
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
            //Email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txtEmail.setError("Enter a valid Email address!");
                txtEmail.requestFocus();
                valid = false;
            } else {
                txtEmail.setError(null);
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
        }

        return valid;
    }

/*    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Collector collector = new Collector();
        collector.setName(txtName.getText().toString());
        collector.setEmail(txtEmail.getText().toString());
        collector.setPhone(txtPhone.getText().toString());
        collector.setAadhar(txtAadhar.getText().toString());
        outState.putSerializable("objCollector", collector);
    }*/

}