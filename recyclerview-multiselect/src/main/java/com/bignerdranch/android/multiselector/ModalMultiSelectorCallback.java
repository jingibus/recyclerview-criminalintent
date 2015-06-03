package com.bignerdranch.android.multiselector;

import android.support.v7.view.ActionMode;
import android.view.Menu;

/**
 * <p>Abstract superclass for creating action mode callbacks that automatically toggle
 * a {@link com.bignerdranch.android.multiselector.MultiSelector}'s
 * {@link com.bignerdranch.android.multiselector.MultiSelector#setSelectable(boolean)}
 * value in {@link android.support.v7.view.ActionMode.Callback#onPrepareActionMode(android.support.v7.view.ActionMode, android.view.Menu)}
 * and {@link android.support.v7.view.ActionMode.Callback#onDestroyActionMode(android.support.v7.view.ActionMode)}.</p>
 */
public abstract class ModalMultiSelectorCallback implements ActionMode.Callback {

    private MultiSelector mMultiSelector;
    private static final String TAG = "modalMultiSelectorCallback";
    private boolean mClearOnPrepare = true;

    public ModalMultiSelectorCallback(MultiSelector multiSelector) {
        mMultiSelector = multiSelector;
    }

    /**
     * <p>Get current value of clearOnPrepare.</p>
     *
     * @return Current value of clearOnPrepare.
     */
    public boolean shouldClearOnPrepare() {
        return mClearOnPrepare;
    }

    /**
     * <p>Setter for clearOnPrepare.
     * When this property is true, {@link #onPrepareActionMode(android.support.v7.view.ActionMode, android.view.Menu)}
     * will call {@link MultiSelector#clearSelections()}.</p>
     *
     * @param clearOnPrepare New value for clearOnPrepare.
     */
    public void setClearOnPrepare(boolean clearOnPrepare) {
        mClearOnPrepare = clearOnPrepare;
    }

    /**
     * <p>Attached {@link com.bignerdranch.android.multiselector.MultiSelector} instance.</p>
     *
     * @return Attached selector.
     */
    public MultiSelector getMultiSelector() {
        return mMultiSelector;
    }

    /**
     * <p>Attach this instance to a new {@link com.bignerdranch.android.multiselector.MultiSelector}.</p>
     *
     * @param multiSelector Selector to attach.
     */
    public void setMultiSelector(MultiSelector multiSelector) {
        mMultiSelector = multiSelector;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        if (mClearOnPrepare) {
            mMultiSelector.clearSelections();
        }
        mMultiSelector.setSelectable(true);
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mMultiSelector.setSelectable(false);
    }


}
