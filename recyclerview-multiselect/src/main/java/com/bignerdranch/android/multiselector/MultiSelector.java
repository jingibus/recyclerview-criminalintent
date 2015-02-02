package com.bignerdranch.android.multiselector;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A class that handles selection logic and dispatches updates to
 * connected instances of {@link com.bignerdranch.android.multiselector.SelectableHolder}.</p>
 *
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
public class MultiSelector extends SelectionManager implements Parcelable {
    public static final String TAG = "multiselector";
    public static final Parcelable.Creator<MultiSelector> CREATOR = new Parcelable.Creator<MultiSelector>() {
        public MultiSelector createFromParcel(Parcel source) {
            return new MultiSelector(source);
        }

        public MultiSelector[] newArray(int size) {
            return new MultiSelector[size];
        }
    };
    private SparseBooleanArray mSelections = new SparseBooleanArray();
    private WeakHolderTracker mTracker = new WeakHolderTracker();
    private boolean mIsSelectable;
    private boolean mShdSelectAll;

    public MultiSelector() {
    }

    private MultiSelector(Parcel in) {
        this.mSelections = in.readSparseBooleanArray();
        this.mTracker = in.readParcelable(WeakHolderTracker.class.getClassLoader());
        this.mIsSelectable = in.readByte() != 0;//retrieve the boolean information
    }

    /**
     * <p>Current value of selectable.</p>
     * @return True if in selection mode.
     */
    public boolean isSelectable() {
        return mIsSelectable;
    }

    /**
     * <p>Toggle whether this MultiSelector is in selection mode or not.
     * {@link com.bignerdranch.android.multiselector.SelectableHolder#setSelectable(boolean)}
     * will be called on any attached holders as well.</p>
     * @param isSelectable True if in selection mode.
     */
    public void setSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;
        refreshAllHolders();
    }

    /**
     * <p>Calls through to {@link #setSelected(int, long, boolean)}.</p>
     *
     * @param holder Holder to set selection value for.
     * @param isSelected Whether the item should be selected.
     */
    public void setSelected(SelectableHolder holder, boolean isSelected) {
        setSelected(holder.getPosition(), holder.getItemId(), isSelected);
    }

    /**
     * <p>Sets whether a particular item is selected. In this implementation, id is
     * ignored, but subclasses may use id instead.</p>
     *
     * <p>If a holder is bound for this position, this will call through to
     * {@link com.bignerdranch.android.multiselector.SelectableHolder#setActivated(boolean)}
     * for that holder.</p>
     *
     * @param position Position to select/unselect.
     * @param id Item id to select/unselect. Ignored in this implementation.
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
     * @param id Item id to select/unselect. Ignored in this implementation.
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
     * <p/>
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
        if (getShdSelectAll()) setSelected(holder, true);
        refreshHolder(holder);
    }

    /**
     * <p>Calls through to {@link #tapSelection(int, long)}.</p>
     *
     * @param holder The holder to tap.
     * @return True if {@link #isSelectable()} and selection was toggled for this item.
     */
    public boolean tapSelection(SelectableHolder holder) {
        return tapSelection(holder.getPosition(), holder.getItemId());
    }

    /**
     * <p>Convenience method to ease invoking selection logic.
     * If {@link #isSelectable()} is true, this method toggles selection
     * for the specified item and returns true. Otherwise, it returns false
     * and does nothing.</p>
     * <p/>
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

        boolean isActivated = mSelections.get(holder.getPosition());
        holder.setActivated(isActivated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseBooleanArray(this.mSelections);
        dest.writeParcelable(this.mTracker, flags);
        dest.writeByte(mIsSelectable ? (byte) 1 : (byte) 0);//write the boolean in form of logical 0 or 1
    }

    @Override
    public void selectAll(boolean shdselectAll) {
        this.mShdSelectAll = shdselectAll;
        for (SelectableHolder holder : mTracker.getTrackedHolders()) {

            setSelected(holder, shdselectAll);
        }
        return;
    }


    public boolean getShdSelectAll() {
        return mShdSelectAll;
    }





}
