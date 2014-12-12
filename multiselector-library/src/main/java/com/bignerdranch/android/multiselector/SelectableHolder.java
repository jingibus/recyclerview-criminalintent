package com.bignerdranch.android.multiselector;

/**
 * Public interface used by selectable items connected to {@link com.bignerdranch.android.multiselector.MultiSelector}.
 */
public interface SelectableHolder {
    /**
     * Turn selection mode on for this holder.
     * @param selectable True if selection mode is on.
     */
    void setSelectable(boolean selectable);

    /**
     * Current selection mode state.
     * @return True if selection mode is on.
     */
    boolean isSelectable();

    /**
     * Set this item to be selected (the activated state, for Views and Drawables)
     * @param activated True if selected/activated.
     */
    void setActivated(boolean activated);

    /**
     * Return true if the item is selected/activated.
     * @return True if selected/activated.
     */
    boolean isActivated();

    /**
     * Returns the adapter position this item is currently bound to.
     * This can (and often will) change; if attached to a {@link com.bignerdranch.android.multiselector.MultiSelector},
     * {@link com.bignerdranch.android.multiselector.MultiSelector#bindHolder(SelectableHolder, int, long)}
     * should be called whenever this value changes.
     * @return Position this holder is currently bound to.
     */
    int getPosition();

    /**
     * Return the item id this item is currently bound to.
     * This can (and often will) change; if attached to a {@link com.bignerdranch.android.multiselector.MultiSelector},
     * {@link com.bignerdranch.android.multiselector.MultiSelector#bindHolder(SelectableHolder, int, long)}
     * should be called whenever this value changes.
     * @return Item id this holder is currently bound to.
     */
    long getItemId();
}
