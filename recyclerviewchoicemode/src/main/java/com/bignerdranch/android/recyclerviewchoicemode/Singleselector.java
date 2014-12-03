package com.bignerdranch.android.recyclerviewchoicemode;

/**
 * Created by bphillips on 12/3/14.
 */
public class Singleselector extends Multiselector {
    @Override
    protected void setSelected(int position, long id, boolean isSelected) {
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
