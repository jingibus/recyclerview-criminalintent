package com.bignerdranch.android.recyclerviewchoicemode;

import android.support.v7.view.ActionMode;
import android.view.Menu;

/**
 * Abstract superclass for creating action mode callbacks that automatically toggle
 * a {@link com.bignerdranch.android.recyclerviewchoicemode.MultiSelector}'s
 * {@link com.bignerdranch.android.recyclerviewchoicemode.MultiSelector#setSelectable(boolean)}
 * value in {@link android.support.v7.view.ActionMode.Callback#onPrepareActionMode(android.support.v7.view.ActionMode, android.view.Menu)}
 * and {@link android.support.v7.view.ActionMode.Callback#onDestroyActionMode(android.support.v7.view.ActionMode)}.
 */
public abstract class ModalMultiSelectorCallback implements ActionMode.Callback {
    private MultiSelector mMultiSelector;

    private boolean mClearOnPrepare = true;

    public ModalMultiSelectorCallback(MultiSelector multiSelector) {
        mMultiSelector = multiSelector;
    }

    /**
     * Get current value of clearOnPrepare.
     * @return Current value of clearOnPrepare.
     */
    public boolean shouldClearOnPrepare() {
        return mClearOnPrepare;
    }

    /**
     * Setter for clearOnPrepare.
     * When this property is true, {@link #onPrepareActionMode(android.support.v7.view.ActionMode, android.view.Menu)}
     * will call {@link MultiSelector#clearSelections()}.
     * @return New value for clearOnPrepare.
     */
    public void setClearOnPrepare(boolean clearOnPrepare) {
        mClearOnPrepare = clearOnPrepare;
    }

    /**
     * Attached {@link com.bignerdranch.android.recyclerviewchoicemode.MultiSelector} instance.
     * @return Attached selector.
     */
    public MultiSelector getMultiSelector() {
        return mMultiSelector;
    }

    /**
     * Attach this instance to a new {@link com.bignerdranch.android.recyclerviewchoicemode.MultiSelector}.
     * @param multiSelector Selector to attach.
     */
    public void setMultiSelector(MultiSelector multiSelector) {
        mMultiSelector = multiSelector;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
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
