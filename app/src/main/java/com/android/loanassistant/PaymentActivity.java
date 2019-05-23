package com.android.loanassistant;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.loanassistant.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    @BindView(R.id.dialog2Name)
    TextView txtName;

    @BindView(R.id.dialog2Phone)
    TextView txtPhone;

    @BindView(R.id.dialog2Amount)
    TextView txtAmount;

    @BindView(R.id.dialog2Address)
    TextView txtAddress;

    @BindView(R.id.etPaidAmt)
    EditText txtPaidAmt;

    @BindView(R.id.dialogPmtMode)
    Spinner spMode;

    @BindView(R.id.btnLocation)
    Button btnLocation;

    @BindView(R.id.btnPay)
    Button btnPay;
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        if (getIntent().hasExtra("keyPhone") && getIntent().hasExtra("keyAmount")) {
            Intent rcvIntent = getIntent();
            phone = rcvIntent.getStringExtra("keyPhone");
            txtPhone.setText(String.format(getString(R.string.style_phone), phone));
            txtAmount.setText(String.format(getString(R.string.style_amount), rcvIntent.getStringExtra("keyAmount")));
        }

        CollectionReference ref = FirebaseFirestore.getInstance().collection("user");
        ref.document(phone).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                txtName.setText(String.format(getString(R.string.style_name), user.getName()));
                txtAddress.setText(String.format(getString(R.string.style_address), user.getAddress()));
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Cash");
        adapter.add("Online");
        spMode.setAdapter(adapter);
        spMode.setPrompt("---Select Payment Mode---");

        btnLocation.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
               /* Bundle bundle = new Bundle();
                bundle.putString("keyAddress", txtAddress.getText().toString());
                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction().replace(R.id.frameLayout2, mapFragment);
                transaction.commit();*/

                //TODO Convert address to latlng and check for null values

                LatLng latLng = getLatLong(txtAddress.getText().toString());
//                if(isValidLatLng(latLng.latitude,latLng.longitude)){}

                Intent intent = new Intent(PaymentActivity.this, MapsActivity.class);
                intent.putExtra("keyAddress", txtAddress.getText().toString());
                startActivity(intent);

            }
        });

        /*spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        txtPaidAmt.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        txtPaidAmt.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        btnPay.setOnClickListener(new View.OnClickListener()

        {
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

    }

    private void cashPayment() {
        String tmpAmt = txtAmount.getText().toString().substring(10);
        int amount = Math.round(Float.parseFloat(tmpAmt));

        int newAmt = Math.round(Float.parseFloat(txtPaidAmt.getText().toString()));
        if (newAmt > amount) {
            Toast.makeText(this, "Enter valid amount", Toast.LENGTH_SHORT).show();
        } else {
            int amt = amount - newAmt;
            CollectionReference ref = FirebaseFirestore.getInstance().collection("loans");
            Map<String, Object> map = new HashMap<>();
            map.put("paid", "cash");
            map.put("amount", String.valueOf(amt));
            map.put("appoint", "");

            ref.document(phone).set(map, SetOptions.mergeFields("appoint", "paid", "amount"))
//            ;ref.document(phone).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("paid", "Mode updated to cash");
                            Toast.makeText(PaymentActivity.this, "Payment successfully collected through cash", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }

    private void startPayment() {
        String tmpAmt = txtAmount.getText().toString().substring(10);
        int amount = Math.round(Float.parseFloat(tmpAmt));
        int newAmt = Math.round(Float.parseFloat(txtPaidAmt.getText().toString()));
        if (newAmt > amount) {
            Toast.makeText(this, "Enter valid amount", Toast.LENGTH_SHORT).show();
        } else {
            int amt = amount - newAmt;
            newAmt *= 100;
            final Checkout checkout = new Checkout();
//        amount=amount+100;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "Demo");
                jsonObject.put("description", "Loan Assistant");
                jsonObject.put("currency", "INR");
                jsonObject.put("amount", newAmt);
                checkout.open(this, jsonObject);
            } catch (JSONException e) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
            String tmpAmt = txtAmount.getText().toString().substring(10);
            int amount = Math.round(Float.parseFloat(tmpAmt));
            int newAmt = Math.round(Float.parseFloat(txtPaidAmt.getText().toString()));
            CollectionReference ref = FirebaseFirestore.getInstance().collection("loans");
            Map<String, Object> map = new HashMap<>();
            map.put("paid", "online");
            map.put("appoint", "");
            map.put("amount", String.valueOf(amount - newAmt));

            ref.document(phone).set(map, SetOptions.mergeFields("appoint", "paid", "amount"))
//            ;ref.document(phone).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("paid", "Mode updated to online");
                            finish();
                        }
                    });
        } catch (Exception e) {
            Log.e("payment", "Exception in onPaymentSuccess", e);
        }
    }


    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("payment", "Exception in onPaymentError", e);
        }
    }

    public boolean isValidLatLng(double lat, double lng) {
        if (lat < -90 || lat > 90) {
            return false;
        } else if (lng < -180 || lng > 180) {
            return false;
        }
        return true;
    }

    private LatLng getLatLong(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } else
                Toast.makeText(this, "Address not accurate", Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
