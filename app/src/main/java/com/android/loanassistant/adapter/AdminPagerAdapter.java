package com.android.loanassistant.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.loanassistant.fragments.AddCollector;

public class AdminPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS=2;

    public AdminPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new AddCollector();
        } else if (position == 1) {
            fragment = new AddCollector();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

  /*  @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Tab-1";
        } else if (position == 1) {
            title = "Tab-2";
        }
        return title;
    }*/
}
