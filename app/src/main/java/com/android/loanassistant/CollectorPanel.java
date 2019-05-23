package com.android.loanassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.loanassistant.fragments.C_Dashboard;
import com.android.loanassistant.fragments.UserPayment;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.NetworkUtil;
import com.android.loanassistant.interfaces.CollectorInterface;
import com.android.loanassistant.model.Appointed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectorPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CollectorInterface{
    @BindView(R.id.nav_view1)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout1)
    DrawerLayout drawer;
    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private C_Dashboard dashboard;
    private UserPayment payment;
//    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_collector_panel);
        initViews();
        checkInternet();

      /*  Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("keyPhone", "2390494846");
        intent.putExtra("keyAmount", "10050.00");
        startActivity(intent);*/
    }

    private void checkInternet() {
           /* Intent intent = new Intent();
            intent.setAction("CheckConnectivity"); sendBroadcast(intent);*/
        if (NetworkUtil.getConnectivityStatus(this) == 0) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(CollectorPanel.this)
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
    }

    private void changePwd(final String email, String pwd) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText txtPwd = dialogView.findViewById(R.id.chngPwd);

        dialogBuilder.setTitle("Change Password \n");
        dialogBuilder.setMessage("Enter new Password from 4 to 10 characters");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                String pwd = txtPwd.getText().toString().trim();
                if (validate(txtPwd, pwd)) {
                    //

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, pwd);

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //updatePassword
                                    } else {
                                        // Password is incorrect
                                    }
                                }
                            });

                    //

                    /*DocumentReference ref = FirebaseFirestore.getInstance().collection("login").document(email);
                    ref.update("password", pwd).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                        }
                    });*/
                }
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private boolean validate(EditText txtPwd, String password) {
        boolean valid = true;
        if (password.isEmpty()) {
            Toast.makeText(this, "Password can't be empty! ", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            if (password.length() < 4 || password.length() > 10) {
                txtPwd.setError("Between 4 and 10 characters");
                txtPwd.requestFocus();
                valid = false;
            } else if (password.equals(Constants.default_pwd)) {
                txtPwd.setError("Password same as default");
                txtPwd.requestFocus();
                valid = false;
            } else {
                txtPwd.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_c:
                /*mAuth.signOut();
                user = null;*/
                Intent intent = new Intent(CollectorPanel.this, LoginScreen.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            /*case R.id.notifications:
                return false;*/
            case R.id.exit:
                AlertDialog alertDialog = new android.app.AlertDialog.Builder(CollectorPanel.this)
                        .setMessage(R.string.exit_dialog)
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
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();
        setSupportActionBar(toolbar);
//        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();
        dashboard = new C_Dashboard();
        dashboard.setCallBack(this);
        payment = new UserPayment();
        payment.setCallBack(this);
        /*mapFragment = new MapFragment();
        mapFragment.setCallBack(this);*/

        transaction = manager.beginTransaction().add(R.id.frameLayout1, dashboard);
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    public void setTitle(int id) {
        getSupportActionBar().setTitle(id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        manager = getSupportFragmentManager();

        /*switch (id) {
            case R.id.add_collector:
                transaction = manager.beginTransaction().replace(R.id.frameLayout, collector);
                transaction.commit();
                break;
            case R.id.details:
                transaction = manager.beginTransaction().replace(R.id.frameLayout, new CollectorDetails());
                transaction.commit();
                break;
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void showUserDetails(Appointed model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("objAppointed", model);
        manager = getSupportFragmentManager();
        payment.setArguments(bundle);
        transaction = manager.beginTransaction().replace(R.id.frameLayout1, payment);
        transaction.commit();
    }

    @Override
    public void showMap(String address) {

    }
/*
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
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
    }*/


}

