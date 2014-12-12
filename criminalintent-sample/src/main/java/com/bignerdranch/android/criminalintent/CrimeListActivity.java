package com.bignerdranch.android.criminalintent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
