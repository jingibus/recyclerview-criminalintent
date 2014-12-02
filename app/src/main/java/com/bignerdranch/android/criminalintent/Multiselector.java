package com.bignerdranch.android.criminalintent;

import android.support.v7.widget.SelectableHolder;
import android.util.SparseBooleanArray;

public class Multiselector {
    private SparseBooleanArray mSelections = new SparseBooleanArray();
    private WeakHolderTracker mTracker = new WeakHolderTracker();

    private boolean mIsSelectable;

    public void setSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;
        refreshAllHolders();
    }

    public boolean isSelectable() {
        return mIsSelectable;
    }

    private void refreshAllHolders() {
        for (SelectableHolder holder : mTracker.getTrackedHolders()) {
            refreshHolder(holder);
        }
    }

    private void refreshHolder(SelectableHolder holder) {
        if (holder == null) {
            return;
        }
        holder.setSelectable(mIsSelectable);
        holder.setActivated(mSelections.get(holder.getPosition()));
    }

    public boolean isSelected(int position, long id) {
        return mSelections.get(position);
    }

    public void setSelected(int position, long id, boolean isSelected) {
        mSelections.put(position, isSelected);
        refreshHolder(mTracker.getHolder(position));
    }

    public void clearSelections() {
        mSelections.clear();
        refreshAllHolders();
    }

    public void bindHolder(SelectableHolder holder, int position, long id) {
        mTracker.bindHolder(holder, position);
        refreshHolder(holder);
    }
}
