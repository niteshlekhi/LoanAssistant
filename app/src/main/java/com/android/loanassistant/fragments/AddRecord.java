package com.android.loanassistant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecord extends Fragment {

    @BindView(R.id.custName)
    EditText txtName;
    @BindView(R.id.custPhone)
    EditText txtPhone;
    @BindView(R.id.custAadhar)
    EditText txtAadhar;
    @BindView(R.id.btnNextPage)
    Button btnAdd;

    private CustomProgressDialog dialog;

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
        dialog = new CustomProgressDialog(getActivity());

        setRetainInstance(true);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
//                    dialog.showDialog(Constants.ADDING);
                    String name = txtName.getText().toString();
                    String phone = txtPhone.getText().toString();
                    String aadhar = txtAadhar.getText().toString();
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
