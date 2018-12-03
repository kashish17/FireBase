package com.example.kashish.movies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MoviePageAdapter extends FragmentPagerAdapter {
    public MoviePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0){
            return new MovieFragment();
        }
        else if(i == 1){
            return new TvFragment();
        }

        return null;

    }

    @Override
    public int getCount() {
        return 2;
    }
}
