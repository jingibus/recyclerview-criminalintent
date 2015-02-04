package com.bignerdranch.android.multiselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;

/**
 * Created by Genius on 2/2/15.
 */
public abstract class SelectionManager {

    /**
     * <p>
     * This is a wiring added just to start selection with action mode. this will call through to
     * {@link android.support.v7.app.ActionBarActivity.startSupportActionMode} or {@link android.app.Activity.startActionMode}</p>
     *
     * @param activity
     * @param callback
     */
    public void startSelectionWithActionMode(Activity activity, ActionMode.Callback callback) {
        activity.startActionMode(callback);
    }

    public void startSupportSelectionWithActionMode(ActionBarActivity activity, android.support.v7.view.ActionMode.Callback callback) {
        activity.startSupportActionMode(callback);
    }

    /**
     * THis is an added functionality for toggling complete selection of all view on and off
     *
     * @param shdselectAll if true is passe then the mode will be to select all view and false otherwise
     */
    public abstract void selectAll(boolean shdselectAll);

    /**
     * This is a functionality added to allow saving  the state of the multiselector
     *
     * @return bundle of the saved states . This can therefore be called Through during saving
     * ie {@link android.app.Activity.onSaveInstanceState()},
     */
    public abstract Bundle saveSelectionStates();

    /**
     * This is a functionality added to allow  restoring the state of the multiselector can be called when the activity is created
     * ie  {@link android.app.Activity.onRestoreInstanceState()}, {@link android.app.Activity.onActivityCreated()}, {@link android.app.Activity.onCreate()},
     * {@link android.app.Activity.onCreateView()},
     */
    public abstract void restoreSelectionStates(Bundle savedStates);

}
