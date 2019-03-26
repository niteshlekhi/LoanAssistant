package com.android.loanassistant;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AdminPanel extends AppCompatActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        initViews();
    }

    private void initViews() {
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
    }
}
