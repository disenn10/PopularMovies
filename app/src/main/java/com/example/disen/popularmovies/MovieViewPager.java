package com.example.disen.popularmovies;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by disen on 10/18/2017.
 */

public class MovieViewPager extends FragmentPagerAdapter {
    public MovieViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Home();
            default:
                return new Home();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Movies";
            default:
                return "Movies";
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
