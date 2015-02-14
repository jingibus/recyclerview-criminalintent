package com.bignerdranch.android.criminalintent;

import android.app.Fragment;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
