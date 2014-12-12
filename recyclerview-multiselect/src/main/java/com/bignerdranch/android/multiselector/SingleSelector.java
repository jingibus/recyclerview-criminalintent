package com.bignerdranch.android.multiselector;

/**
 * <p>A MultiSelector that only allows for one position at a time to be selected. </p>
 * <p>Any time {@link #setSelected(int, long, boolean)} is called, all other selected positions are set to false.</p>
 */
public class SingleSelector extends MultiSelector {
    @Override
    public void setSelected(int position, long id, boolean isSelected) {
        if (isSelected) {
            for (Integer selectedPosition : getSelectedPositions()) {
                if (selectedPosition != position) {
                    super.setSelected(selectedPosition, 0, false);
                }
            }
        }
        super.setSelected(position, id, isSelected);
    }
}
