package com.android.loanassistant;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.android.loanassistant.fragments.C_Dashboard;
import com.android.loanassistant.helper.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectorPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view1)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout1)
    DrawerLayout drawer;
    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_collector_panel);
        initViews();
        /*Intent rcvIntent = getIntent();
        String pwd=rcvIntent.getStringExtra("resetPwd");
        if (pwd.equals(Constants.default_pwd))
            changePwd(rcvIntent.getStringExtra("email"));*/
    }

    private void changePwd(final String email) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText txtPwd = dialogView.findViewById(R.id.chngPwd);

        dialogBuilder.setTitle("Change Password");
        dialogBuilder.setMessage("Enter new Password between 4 to 10 characters");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                String pwd = txtPwd.getText().toString().trim();
                if (validate(txtPwd, pwd)) {
                    DocumentReference ref = FirebaseFirestore.getInstance().collection("login").document(email);
                    ref.update("password", pwd).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                        }
                    });
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
        transaction = manager.beginTransaction().add(R.id.frameLayout1, new C_Dashboard());
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    public void setTitle(int id) {
        getSupportActionBar().setTitle(id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
