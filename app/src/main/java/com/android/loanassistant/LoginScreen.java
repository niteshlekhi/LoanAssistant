package com.android.loanassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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

import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.helper.NetworkUtil;
import com.android.loanassistant.model.Collector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.loanassistant.helper.Constants.PREF_EMAIL;
import static com.android.loanassistant.helper.Constants.SIGN_IN;

public class LoginScreen extends AppCompatActivity {

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
    private DocumentReference docRef;
    private FirebaseAuth mAuth;
    private char loginType = 'a';
    private Map<String, ?> keys;
    private ArrayAdapter<String> adapter;
    private CustomProgressDialog dialog;
    private List<Collector> model;
    private List<String> list;
    private boolean checkAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        init();

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
        prefs = getApplicationContext().getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);
        editor = prefs.edit();
        dialog = new CustomProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        txtEmail.setHintTextColor(Color.BLACK);
        txtPassword.setHintTextColor(Color.BLACK);
        txtEmail.requestFocus();
        model = new ArrayList<>();
        list = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        model = task.getResult().toObjects(Collector.class);
                        for (Collector collector : model) {
                            list.add(collector.getEmail());
                        }
                    }
                });
            }
        });
    }

    private void login(final View v) {
        if (validate()) {
            dialog.showDialog(SIGN_IN);
            final String email = txtEmail.getText().toString();
            final String password = txtPassword.getText().toString();
            if (prefs.getAll().containsValue(email)) {
            } else {
                editor.putString(String.valueOf(prefs.getAll().size() + 1), email);
                editor.apply();
            }


            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    dialog.stopDialog();
                    dialog.hideKeyboard(v);
                    if (isAdmin(email)) {
                        Toast.makeText(LoginScreen.this, R.string.admin_success, Toast.LENGTH_SHORT).show();
                        final Intent intent = new Intent(LoginScreen.this, AdminPanel.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginScreen.this, R.string.collector_success, Toast.LENGTH_SHORT).show();
                        final Intent intent = new Intent(LoginScreen.this, CollectorPanel.class);
                        intent.putExtra("resetPwd", password);
                        intent.putExtra("email", email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
//                            finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.stopDialog();
                    dialog.hideKeyboard(v);
                    Toast.makeText(LoginScreen.this, "Invalid login details", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(this, "Check the fields again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAdmin(final String email) {
        checkAdmin = true;

        ref.document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        checkAdmin = false;
//                        Log.d("docref", "Document exists!");
                    }
                }
            }
        });

        for (String s : list) {
            if (s.equals(email)) {
                checkAdmin = false;
                break;
            }
        }

        model.clear();

        return checkAdmin;
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        checkInternet();
    }

    private int checkInternet() {
        int status=-1;
           /* Intent intent = new Intent();
            intent.setAction("CheckConnectivity"); sendBroadcast(intent);*/
        if (NetworkUtil.getConnectivityStatus(this) == 0) {
            status=0;
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LoginScreen.this)
                    .setTitle("Network Error").setMessage(R.string.no_network)
                    .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            checkInternet();
                            finish();
                        }
                    }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog1.dismiss();
                        }
                    }).show();
        }
        return status;
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

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            if (isAdmin(currentUser.getEmail())) {
                Intent intent = new Intent(this, AdminPanel.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
//            Toast.makeText(this, "Not null", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, CollectorPanel.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        } else {
        }
    }
}
