package com.android.loanassistant.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.interfaces.CollectorInterface;
import com.android.loanassistant.model.Appointed;
import com.android.loanassistant.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPayment extends Fragment{

    @BindView(R.id.dialog2Name)
    TextView txtName;

    @BindView(R.id.dialog2Phone)
    TextView txtPhone;

    @BindView(R.id.dialog2Amount)
    TextView txtAmount;

    @BindView(R.id.dialog2Address)
    TextView txtAddress;

    @BindView(R.id.dialogPmtMode)
    Spinner spMode;

    @BindView(R.id.btnLocation)
    Button btnLocation;

    @BindView(R.id.btnPay)
    Button btnPay;

    private CollectorInterface collectorInterface;

    public UserPayment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_payment, container, false);
//        getActivity().setTitle(Constants.ADD_COLLECTOR);
        ButterKnife.bind(this, view);

        String phone = "";

        /*if ((savedInstanceState != null) && (savedInstanceState.getSerializable("objAppointed") != null)) {
            Appointed appointed = (Appointed) savedInstanceState
                    .getSerializable("objAppointed");
            txtPhone.setText(String.format(getString(R.string.style_phone), appointed.getPhone()));
            txtAmount.setText(String.format(getString(R.string.style_amount), appointed.getAmount()));
            phone = appointed.getPhone();
        }*/

        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bundle = getArguments();
            Appointed appointed = (Appointed) bundle.getSerializable("objAppointed");
            txtPhone.setText(String.format(getString(R.string.style_phone), appointed.getPhone()));
            txtAmount.setText(String.format(getString(R.string.style_amount), appointed.getAmount()));
            phone = appointed.getPhone();
        }


        CollectionReference ref = FirebaseFirestore.getInstance().collection("user");
        ref.document(phone).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                txtName.setText(String.format(getString(R.string.style_name), user.getName()));
                txtAddress.setText(String.format(getString(R.string.style_name), user.getAddress()));
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Cash");
        adapter.add("Online");
        spMode.setAdapter(adapter);
        spMode.setPrompt("---Select Payment Mode---");

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               collectorInterface.showMap();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spMode.getSelectedItemPosition()) {
                    case 0:
                        cashPayment();
                        break;
                    case 1:
                        startPayment();
                        break;
                }
            }
        });

        return view;
    }

    private void cashPayment() {
        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
    }

    private void startPayment() {
        final Checkout checkout = new Checkout();
        int amount = Integer.parseInt(txtAmount.getText().toString());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", txtName.getText().toString());
            jsonObject.put("description", "Loan Assistant");
            jsonObject.put("currency", "INR");
            jsonObject.put("amount", amount);
            checkout.open(getActivity(), jsonObject);
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setCallBack(CollectorInterface callBackInterface) {
        this.collectorInterface = callBackInterface;
    }

    /*private boolean validate() {
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
    }*/

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