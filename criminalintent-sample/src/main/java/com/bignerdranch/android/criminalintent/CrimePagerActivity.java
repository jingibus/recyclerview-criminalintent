package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    ViewPager mViewPager;

    public static ActivityOptions getTransition(Activity activity, View crimeView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            crimeView.setTransitionName("crime");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                    crimeView, "crime");

            return options;
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setTransitionName("crime");
        }
        setContentView(mViewPager);

        final ArrayList<Crime> crimes = CrimeLab.get(this).getCrimes();

        FragmentManager fm = getFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return crimes.size();
            }

            @Override
            public Fragment getItem(int pos) {
                UUID crimeId = crimes.get(pos).getId();
                return CrimeFragment.newInstance(crimeId);
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
