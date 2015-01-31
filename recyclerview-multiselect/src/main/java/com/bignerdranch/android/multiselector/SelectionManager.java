package com.bignerdranch.android.multiselector;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;

import java.io.Serializable;

/**
 * Created by Genius on 2/2/15.
 */
public abstract class SelectionManager implements Serializable {

    public void startSelectionWithActionMode(Activity activity, ActionMode.Callback callback) {
        activity.startActionMode(callback);
    }

    public void startSupportSelectionWithActionMode(ActionBarActivity activity, android.support.v7.view.ActionMode.Callback callback) {
        activity.startSupportActionMode(callback);
    }

    public abstract void selectAll(boolean shdselectAll);

}
