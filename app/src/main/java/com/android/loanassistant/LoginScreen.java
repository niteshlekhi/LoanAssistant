package com.android.loanassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.model.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
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
    private char loginType = 'a';
    private Map<String, ?> keys;
    private ArrayAdapter<String> adapter;
    private CustomProgressDialog dialog;
    boolean validLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_screen);
        init();

        Intent intent = new Intent(this, AdminPanel.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        keys = prefs.getAll();
        if (!keys.isEmpty()) {
            for (Map.Entry<String, ?> map : keys.entrySet()) {
                adapter.add((String) map.getValue());
            }
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
                login(v);
            }
        });

    }

    private void init() {
        ButterKnife.bind(this);
        firestore = FirebaseFirestore.getInstance();
        ref = firestore.collection("login");
        prefs = getApplicationContext().getSharedPreferences(Constants.PREF_EMAIL, MODE_PRIVATE);
        editor = prefs.edit();
        dialog = new CustomProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        txtEmail.requestFocus();
        spinner.setItems(Constants.administrator, Constants.collector);
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

    private void login(final View v) {
        validLogin = true;
        if (validate()) {
            dialog.showDialog(Constants.SIGN_IN);
            final String email = txtEmail.getText().toString();
            final String password = txtPassword.getText().toString();
            if (prefs.getAll().containsValue(email)) {
            } else {
                editor.putString(String.valueOf(prefs.getAll().size() + 1), email);
                editor.apply();
            }

            ref.whereEqualTo("email", email)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size() == 1) {
                        Login model = queryDocumentSnapshots.getDocuments().get(0).toObject(Login.class);
                        if (model.getPassword().equals(password) && model.getType() == 0) {
                            dialog.stopDialog();
//                            Toast.makeText(LoginScreen.this, "Admin successful", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(LoginScreen.this, AdminPanel.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (model.getPassword().equals(Constants.default_pwd) && model.getType() == 1) {
                            dialog.stopDialog();
                            dialog.hideKeyboard(v);
//                            Toast.makeText(LoginScreen.this, "Collector successful", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(LoginScreen.this, CollectorPanel.class);
                            intent.putExtra("resetPwd", 1);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (model.getPassword().equals(password) && model.getType() == 1) {
                            dialog.stopDialog();
                            dialog.hideKeyboard(v);
//                            Toast.makeText(LoginScreen.this, "Collector successful", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(LoginScreen.this, CollectorPanel.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            dialog.stopDialog();
                            dialog.hideKeyboard(v);
                            Toast.makeText(LoginScreen.this, "Invalid login details", Toast.LENGTH_SHORT).show();
                        }

                    } else if (queryDocumentSnapshots.size() > 1)
                        Toast.makeText(LoginScreen.this, "Email Duplicacy!!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginScreen.this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginScreen.this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(this, "Check the fields again", Toast.LENGTH_SHORT).show();
        }
    }

    private void collectorLogin(String email) {
        final Intent intent = new Intent(LoginScreen.this, CollectorPanel.class);
        Query query = ref.whereEqualTo("email", email).whereEqualTo("password", Constants.default_pwd);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                intent.putExtra("resetPwd", 1);
                Toast.makeText(LoginScreen.this, "Collector successful", Toast.LENGTH_SHORT).show();
            }
        });
//        dialog.hideKeyboard(getWindow().getDecorView().getRootView());
        /* intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }

    public boolean validate() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields ", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txtEmail.setError("Enter a valid Email address!");
                txtEmail.requestFocus();
                valid = false;
            } else {
                txtEmail.setError(null);
            }

            if (password.length() < 4 || password.length() > 10) {
                txtPassword.setError("Between 4 and 10 characters");
                txtPassword.requestFocus();
                valid = false;
            } else {
                txtPassword.setError(null);
            }
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

//        updateUI(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LoginScreen.this)
                .setMessage(R.string.exit_dialog)
                .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        finish();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.exit:
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LoginScreen.this)
                        .setMessage(R.string.exit_dialog)
                        .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                finish();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        }).show();
                break;
            default:
        }
        return true;
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
