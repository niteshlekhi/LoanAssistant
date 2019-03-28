package com.android.loanassistant.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.model.Collector;
import com.android.loanassistant.model.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCollector extends Fragment {

    @BindView(R.id.ctrName)
    EditText txtName;

    @BindView(R.id.ctrEmail)
    EditText txtEmail;

    @BindView(R.id.ctrPhone)
    EditText txtPhone;

    @BindView(R.id.ctrAadhar)
    EditText txtAadhar;

    @BindView(R.id.btnAddCollector)
    Button btnAdd;
    private CustomProgressDialog dialog;

    public AddCollector() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_collector, container, false);
        getActivity().setTitle(Constants.ADD_COLLECTOR);
        dialog = new CustomProgressDialog(getActivity());
        ButterKnife.bind(this, view);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    dialog.showDialog(Constants.ADDING);
                    String name = txtName.getText().toString();
                    final String email = txtEmail.getText().toString();
                    String phone = txtPhone.getText().toString();
                    String aadhar = txtAadhar.getText().toString();
                    final Collector collector = new Collector(name, email, phone, aadhar);
                    CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
                    final CollectionReference ref1 = FirebaseFirestore.getInstance().collection("login");

                    ref.document(email).set(collector).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ref1.document(email).set(new Login(email, Constants.default_pwd, Constants.type_c));
                            dialog.stopDialog();
                            Toast.makeText(getActivity(), "Collector Added!", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.stopDialog();
                            Toast.makeText(getActivity(), "Collector NOT Added!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    private boolean validate() {
        boolean valid = true;
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        String aadhar = txtAadhar.getText().toString();

        if (email.isEmpty() || email.isEmpty() || phone.isEmpty() || aadhar.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields ", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
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

    private void clearData() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAadhar.setText("");
    }

}