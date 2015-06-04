package com.bignerdranch.android.multiselector;

import android.os.Bundle;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A class that handles selection logic and dispatches updates to
 * connected instances of {@link com.bignerdranch.android.multiselector.SelectableHolder}.</p>
 * <p>The out-of-the-box use of MultiSelector is to team it up with {@link com.bignerdranch.android.multiselector.SwappingHolder}
 * for easy selection functionality in {@link android.support.v7.widget.RecyclerView}s. Like this:</p>
 * <pre>
 * {@code
 *
 * private MultiSelector mMultiSelector = new MultiSelector();
 *
 * public class MyViewHolder extends SwappingHolder
 *         implements View.OnClickListener {
 *     public MyViewHolder(View itemView) {
 *         super(itemView, mMultiSelector);
 *         itemView.setOnClickListener(this);
 *     }
 *
 *     public void onClick(View view) {
 *         if (!mMultiSelector.tapSelection(this)) {
 *             // Item was clicked outside of selection mode
 *         }
 *     }
 * }
 * }
 * </pre>
 */
public class MultiSelector {
    private static final String SELECTION_POSITIONS = "position";
    private static final String SELECTIONS_STATE = "state";
    private SparseBooleanArray mSelections = new SparseBooleanArray();
    private WeakHolderTracker mTracker = new WeakHolderTracker();
    private boolean mIsSelectable;

    public MultiSelector() {
    }

    /**
     * <p>Current value of selectable.</p>
     *
     * @return True if in selection mode.
     */
    public boolean isSelectable() {
        return mIsSelectable;
    }

    /**
     * <p>Toggle whether this MultiSelector is in selection mode or not.
     * {@link com.bignerdranch.android.multiselector.SelectableHolder#setSelectable(boolean)}
     * will be called on any attached holders as well.</p>
     *
     * @param isSelectable True if in selection mode.
     */
    public void setSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;
        refreshAllHolders();
    }

    /**
     * <p>Calls through to {@link #setSelected(int, long, boolean)}.</p>
     *
     * @param holder     Holder to set selection value for.
     * @param isSelected Whether the item should be selected.
     */
    public void setSelected(SelectableHolder holder, boolean isSelected) {
        setSelected(holder.getAdapterPosition(), holder.getItemId(), isSelected);
    }

    /**
     * <p>Sets whether a particular item is selected. In this implementation, id is
     * ignored, but subclasses may use id instead.</p>
     * <p>If a holder is bound for this position, this will call through to
     * {@link com.bignerdranch.android.multiselector.SelectableHolder#setActivated(boolean)}
     * for that holder.</p>
     *
     * @param position   Position to select/unselect.
     * @param id         Item id to select/unselect. Ignored in this implementation.
     * @param isSelected Whether the item will be selected.
     */
    public void setSelected(int position, long id, boolean isSelected) {
        mSelections.put(position, isSelected);
        refreshHolder(mTracker.getHolder(position));
    }

    /**
     * <p>Returns whether a particular item is selected.</p>
     *
     * @param position The position to test selection for.
     * @param id       Item id to select/unselect. Ignored in this implementation.
     * @return Whether the item is selected.
     */
    public boolean isSelected(int position, long id) {
        return mSelections.get(position);
    }

    /**
     * <p>Sets selected to false for all positions. Will refresh
     * all bound holders.</p>
     */
    public void clearSelections() {
        mSelections.clear();
        refreshAllHolders();
    }

    /**
     * <p>Return a list of selected positions.</p>
     *
     * @return A list of the currently selected positions.
     */
    public List<Integer> getSelectedPositions() {
        List<Integer> positions = new ArrayList<Integer>();

        for (int i = 0; i < mSelections.size(); i++) {
            if (mSelections.valueAt(i)) {
                positions.add(mSelections.keyAt(i));
            }
        }

        return positions;
    }

    /**
     * <p>Bind a holder to a specific position/id. This implementation ignores the id.</p>
     * <p>Bound holders will receive calls to {@link com.bignerdranch.android.multiselector.SelectableHolder#setSelectable(boolean)}
     * and {@link com.bignerdranch.android.multiselector.SelectableHolder#setActivated(boolean)} when
     * {@link #setSelectable(boolean)} is called, or when {@link #setSelected(int, long, boolean)} is called for the
     * associated position, respectively.</p>
     *
     * @param holder   A holder to bind.
     * @param position Position the holder will be bound to.
     * @param id       Item id the holder will be bound to. Ignored in this implementation.
     */
    public void bindHolder(SelectableHolder holder, int position, long id) {
        mTracker.bindHolder(holder, position);
         refreshHolder(holder);
    }

    /**
     * <p>Calls through to {@link #tapSelection(int, long)}.</p>
     *
     * @param holder The holder to tap.
     * @return True if {@link #isSelectable()} and selection was toggled for this item.
     */
    public boolean tapSelection(SelectableHolder holder) {
        return tapSelection(holder.getAdapterPosition(), holder.getItemId());
    }

    /**
     * <p>Convenience method to ease invoking selection logic.
     * If {@link #isSelectable()} is true, this method toggles selection
     * for the specified item and returns true. Otherwise, it returns false
     * and does nothing.</p>
     * <p>Equivalent to:</p>
     * <pre>
     * {@code
     * if (multiSelector.isSelectable()) {
     *     boolean isSelected = isSelected(position, itemId);
     *     setSelected(position, itemId, !isSelected);
     *     return true;
     * } else {
     *     return false;
     * }
     * }
     * </pre>
     *
     * @param position Position to tap.
     * @param itemId   Item id to tap. Ignored in this implementation.
     * @return True if the item was toggled.
     */
    public boolean tapSelection(int position, long itemId) {
        if (mIsSelectable) {
            boolean isSelected = isSelected(position, itemId);
            setSelected(position, itemId, !isSelected);
            return true;
        } else {
            return false;
        }

    }

    public void refreshAllHolders() {
        for (SelectableHolder holder : mTracker.getTrackedHolders()) {
            refreshHolder(holder);
        }
    }

    private void refreshHolder(SelectableHolder holder) {
        if (holder == null) {
            return;
        }
        holder.setSelectable(mIsSelectable);

        boolean isActivated = mSelections.get(holder.getAdapterPosition());
        holder.setActivated(isActivated);
    }


    /**
     * @return Bundle containing the states of the selection and a flag indicating if the multiselection is in
     * selection mode or not
     */

    public Bundle saveSelectionStates() {
        Bundle information = new Bundle();
        information.putIntegerArrayList(SELECTION_POSITIONS, (ArrayList<Integer>) getSelectedPositions());
        information.putBoolean(SELECTIONS_STATE, isSelectable());
        return information;
    }

    /**
     * restore the selection states of the multiselector and the ViewHolder Trackers
     *
     * @param savedStates Saved state bundle, probably from a fragment or activity.
     */

    public void restoreSelectionStates(Bundle savedStates) {
        List<Integer> selectedPositions = savedStates.getIntegerArrayList(SELECTION_POSITIONS);
        restoreSelections(selectedPositions);
        mIsSelectable = savedStates.getBoolean(SELECTIONS_STATE);

    }

    private void restoreSelections(List<Integer> selected) {
        if (selected == null) return;
        int position;
        mSelections.clear();
        for (int i = 0; i < selected.size(); i++) {
            position = selected.get(i);
            mSelections.put(position, true);
        }
        refreshAllHolders();
    }


}
