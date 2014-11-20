package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CrimeListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private boolean mIsInSelectionMode = false;
    private Set<Crime> mSelectedCrimes = new HashSet<Crime>();

    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);
        setRetainInstance(true);
        mSubtitleVisible = false;
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, parent, false);

        if (mSubtitleVisible) {
            getActionBar().setSubtitle(R.string.subtitle);
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimes = CrimeLab.get(getActivity()).getCrimes();
        mRecyclerView.setAdapter(new CrimeAdapter());


        return v;
    }

    private void selectCrime(Crime c) {
        // start an instance of CrimePagerActivity
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int index = mCrimes.indexOf(c);
            CrimeHolder holder = (CrimeHolder)mRecyclerView
                    .findViewHolderForPosition(index);

            ActivityOptions options = CrimePagerActivity.getTransition(
                    getActivity(), holder.itemView);

            startActivityForResult(i, 0, options.toBundle());
        } else {
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    private ActionMode.Callback deleteMode = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            mIsInSelectionMode = true;
            mSelectedCrimes = new HashSet<Crime>();
            mRecyclerView.getAdapter().notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_item_delete_crime:
                    for (Crime crime : mSelectedCrimes) {
                        mRecyclerView.getAdapter().notifyItemRemoved(mCrimes.indexOf(crime));
                        CrimeLab.get(getActivity()).deleteCrime(crime);
                    }

                    // We used to finish the action mode here. Doing that
                    // breaks the animations in RecyclerView, though,
                    // because finishing the actionMode triggers a refresh on
                    // RecyclerView. This appears to do a notifyDataSetChanged(),
                    // which is no longer what we want to recommend for updates, as
                    // it provides no animation.
                    //
                    // actionMode.finish();
                    return true;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mIsInSelectionMode = false;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                final Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);

                // NOTE: RecycleView animation code
                // This implementation does it the hard way, kicking off an animation
                // and waiting until it's done to open the crime activity.
                //
                // Another option that might be better is to
                mRecyclerView.getAdapter().notifyItemInserted(mCrimes.indexOf(crime));

                // The idea here is to have the view animate in, then trigger opening it up in a new window.
                // Unfortunately, this does not work because the animation is not immediately triggered
                // when you call notifyItemInserted.
//                mRecyclerView.getItemAnimator().isRunning(
//                        new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
//                    @Override
//                    public void onAnimationsFinished() {
//                        selectCrime(crime);
//                    }
//                });
                return true;
            case R.id.menu_item_show_subtitle:
                ActionBar actionBar = getActionBar();
            	if (actionBar.getSubtitle() == null) {
                    actionBar.setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
            	}  else {
            		actionBar.setSubtitle(null);
            		 mSubtitleVisible = false;
            		item.setTitle(R.string.show_subtitle);
            	}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } 
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }


    private class CrimeHolder extends SelectableHolder implements View.OnClickListener {
        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_list_item_titleTextView);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_list_item_dateTextView);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ActionBarActivity activity = (ActionBarActivity)getActivity();
                    activity.startSupportActionMode(deleteMode);
                    toggleSelection();
                    return true;
                }
            });
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
            mSolvedCheckBox.setChecked(crime.isSolved());

            if (mIsInSelectionMode && mSelectedCrimes.contains(mCrime)) {
                setActivated(true);
            } else {
                setActivated(false);
            }
        }

        @Override
        public void onClick(View v) {
            if (mCrime == null) {
                return;
            }
            if (mIsInSelectionMode) {
                toggleSelection();
            } else {
                selectCrime(mCrime);
            }
        }

        private void toggleSelection() {
            setActivated(!isActivated());
//            itemView.setActivated(!itemView.isActivated());
//            ColorStateList colorStateList =
//                    getActivity().getResources()
//                    .getColorStateList(R.color.background);
//
            if (mSelectedCrimes.contains(mCrime)) {
                mSelectedCrimes.remove(mCrime);
            } else {
                mSelectedCrimes.add(mCrime);
            }
//
//            itemView.setActivated(mSelectedCrimes.contains(mCrime));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int pos) {
            Crime crime = mCrimes.get(pos);
            holder.bindCrime(crime);
            holder.setSelectable(mIsInSelectionMode);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}

