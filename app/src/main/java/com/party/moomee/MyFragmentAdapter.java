package com.party.moomee;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 : return new HomeFragment();
            case 1 : return new ListFragment();
            default : return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        /*String[] tabs = {"หน้าหลัก","อาหารที่มีอยู่"};
        return tabs[position];*/
        return null;
    }


}
