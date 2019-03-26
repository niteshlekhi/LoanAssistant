package com.android.loanassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.loanassistant.helper.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginScreen extends AppCompatActivity {
    @BindView(R.id.spLogin)
    MaterialSpinner spinner;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.loginEmail)
    AutoCompleteTextView txtEmail;
    @BindView(R.id.loginPassword)
    EditText txtPassword;


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore firestore;
    private CollectionReference ref;
    private FirebaseAuth mAuth;
    private char loginType = 'n';
    private Map<String, ?> keys;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_screen);
        init();

        keys = prefs.getAll();
        for (Map.Entry<String, ?> map : keys.entrySet()) {
            adapter.add(map.getKey());
        }
        txtEmail.setAdapter(adapter);
        txtEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtEmail.setText((String) parent.getItemAtPosition(position));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void init() {
        ButterKnife.bind(this);
        firestore = FirebaseFirestore.getInstance();
        ref = firestore.collection("users");
        prefs = getApplicationContext().getSharedPreferences(Constants.PREF_EMAIL, MODE_PRIVATE);
        editor = prefs.edit();
        mAuth=FirebaseAuth.getInstance();

        spinner.setItems(R.string.administrator,R.string.collector);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0)
                    loginType = 'a';
                else if (position == 1)
                    loginType = 'c';
            }
        });
    }

    private void login() {
        if (validate()) {
            String email = txtEmail.getText().toString();
            if (prefs.getAll().containsValue(email)) {
            } else {
                editor.putString(String.valueOf(prefs.getAll().size() + 1), email);
                editor.apply();
            }
            if(loginType=='c')
            {
                Intent intent=new Intent(this,CollectorMain.class);
                startActivity(intent);
            }
            else{
                Intent intent=new Intent(this,AdminPanel.class);
                startActivity(intent);
            }
            finish();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Enter a valid email address!");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            txtPassword.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

//        updateUI(user);
    }

    /*private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(this, AdminPanel.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
        }
    }*/
}
