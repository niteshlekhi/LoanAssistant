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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.loanassistant.fragments.AddCollector;
import com.android.loanassistant.fragments.CollectorDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        initViews();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_admin:
                user = null;
                mAuth.signOut();
                Intent intent = new Intent(AdminPanel.this, LoginScreen.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            /*case R.id.notifications:
                return false;*/
            case R.id.exit:
                AlertDialog alertDialog = new android.app.AlertDialog.Builder(AdminPanel.this)
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
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction().add(R.id.frameLayout, new AddCollector());
        transaction.commit();

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

        switch (id) {
            case R.id.add_collector:
                transaction = manager.beginTransaction().replace(R.id.frameLayout, new AddCollector());
                transaction.commit();
                break;
            case R.id.details:
                transaction = manager.beginTransaction().replace(R.id.frameLayout, new CollectorDetails());
                transaction.commit();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
