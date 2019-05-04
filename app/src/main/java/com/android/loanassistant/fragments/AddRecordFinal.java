package com.android.loanassistant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecordFinal extends Fragment {

    @BindView(R.id.loanAmount)
    EditText txtAmount;

    @BindView(R.id.interestRate)
    EditText txtRate;

    @BindView(R.id.duration)
    EditText txtDuration;

    @BindView(R.id.spDuration)
    Spinner spDuration;

    @BindView(R.id.btnAddRecord)
    Button btnReview;

    private CustomProgressDialog dialog;

    public AddRecordFinal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_record2, container, false);
        getActivity().setTitle(Constants.ADD_RECORD);
        spDuration=new Spinner(getActivity());
        ArrayAdapter spAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item);
        spAdapter.add("monthly");
        spAdapter.add("weekly");
        spDuration.setAdapter(spAdapter);

        spDuration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:


                }
            }
        });

        ButterKnife.bind(this, view);
        dialog = new CustomProgressDialog(getActivity());

        setRetainInstance(true);

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
//                    dialog.showDialog(Constants.ADDING);
                    double amount = Double.parseDouble(txtAmount.getText().toString());
                    double rate = Double.parseDouble(txtRate.getText().toString());
                    int time = Integer.parseInt(txtDuration.getText().toString());


//                    float amount = Float.parseFloat(txtAmount.getText().toString());
//                    float rate = Float.parseFloat(txtinterest.getText().toString());

//                    Collector collector = new Collector(name, email, phone, aadhar);
//                    CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");

              /*      ref.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() > 0) {
                                    Toast.makeText(getActivity(), "Email already exists", Toast.LENGTH_SHORT).show();
                                    dialog.stopDialog();
                                } else {
                                }
                            } else {
                            }
                        }
                    });*/

                    /*ref.document(email).set(collector).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Collector Added!", Toast.LENGTH_SHORT).show();
                            dialog.stopDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Collector NOT Added!", Toast.LENGTH_SHORT).show();
                            dialog.stopDialog();
                        }
                    });*/
                }
            }
        });


        return view;
    }

    private boolean validate() {
        boolean valid = true;


        return valid;
    }

}
