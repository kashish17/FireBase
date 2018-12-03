package com.example.kashish.movies;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {


    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // Toast.makeText(getContext(),"Fav",Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarF);
//        ((AppCompatActivity)getActivity()).getSupportActionBar(toolbar);


        ViewPager viewPager = view.findViewById(R.id.viewpagerF);
        FavPageAdapter adapter = new FavPageAdapter(getChildFragmentManager());

        TabLayout tabLayout = view.findViewById(R.id.tabLayoutF);
        //  tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //    tabLayout.setInlineLabel(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setAdapter(adapter);




        return  view;
    }

}
