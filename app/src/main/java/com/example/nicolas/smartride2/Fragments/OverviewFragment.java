package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.GRAVITY_FILL;
import static com.example.nicolas.smartride2.R.layout.settings;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class OverviewFragment extends Fragment {

    TabLayout tabs;
    SettingsManager settings;
    private List<String> mFragmentTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    Adapter adapter;
    ViewPager viewPager;
    //int mNum;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //savedInstanceState.putParcelable("Fragments",mFragmentList);
        //mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setListAdapter(new ArrayAdapter<String>(getActivity(),
                //android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settings = SmartRide.getSettingsManager();
        adapter = new Adapter(getChildFragmentManager(),mFragmentList,mFragmentTitleList);
        //adapter = new Adapter(getFragmentManager());
        View overView = inflater.inflate(R.layout.overview, container, false);
        viewPager = (ViewPager) overView.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1); //TODO GERER STATE
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

        System.out.println("NB RUNS FOR OVERVIEW = "+String.valueOf(settings.getRunNbPref()));
        if (settings.getFinishedMotionRunPref() || settings.getFinishedManualRunPref()) {
            if (settings.getRunNbPref()==1) {
                mFragmentList.add(new ActualRideFragment());
                mFragmentTitleList.add("Actual Ride");
            }
            else if (settings.getRunNbPref()>=2) {
                mFragmentList.add(new ActualRideFragment());
                mFragmentTitleList.add("Actual Ride");
                mFragmentList.add(new LastRideFragment());
                mFragmentTitleList.add("Last Ride");
            }
        }
        else {
            mFragmentList.add(new NoRideFragment());
            mFragmentTitleList.add("No Ride");
            System.out.println(adapter.getPageTitle(0));
        }
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentStatePagerAdapter {
        //private final static List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();
        private List<Fragment> mFragmentList = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm, List<Fragment> mFragmentList, List<String> mFragmentTitleList) {
            super(fm);
            this.mFragmentList=mFragmentList;
            this.mFragmentTitleList=mFragmentTitleList;
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

        /*public void removeFragment(int num) {
            mFragmentList.remove(num);
            mFragmentTitleList.remove(num);
        }

        public void replaceFragment() {
            String title = "Last Ride";
            mFragmentList.add(getItem(0));
            mFragmentTitleList.add(title);
        }*/

        @Override
        public Parcelable saveState()
        {
            return null;
        }

    }


}
