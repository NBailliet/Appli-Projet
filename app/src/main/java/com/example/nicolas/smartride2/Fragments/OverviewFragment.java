package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class OverviewFragment extends Fragment {

    TabLayout tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View overView = inflater.inflate(R.layout.overview, container, false);
        ViewPager viewPager = (ViewPager) overView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs = (TabLayout) overView.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.post(mTabLayout_config);
        return overView;
    }

        Runnable mTabLayout_config = new Runnable()
        {
            @Override
            public void run()
            {

                if(tabs.getWidth() < OverviewFragment.this.getResources().getDisplayMetrics().widthPixels)
                {
                    tabs.setTabMode(TabLayout.MODE_FIXED);
                    ViewGroup.LayoutParams mParams = tabs.getLayoutParams();
                    mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    tabs.setLayoutParams(mParams);

                }
                else
                {
                    tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
            }
        };

    private void setupViewPager(ViewPager viewPager) {

        SettingsManager settings;
        settings = SmartRide.getSettingsManager();

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ActualRideFragment(), "Actual Ride");
        if (settings.getRunPref()==true) {
            adapter.addFragment(new LastRideFragment(), "Last Ride");
        }
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
