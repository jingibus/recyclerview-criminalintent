package com.bignerdranch.android.criminalintent;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;

/**
 * A holder extended to support having a selectable mode with a different
 * background and state list animator.
 *
 */
public abstract class SelectableHolder extends RecyclerView.ViewHolder {
    private Drawable mOriginalDrawable;

    public SelectableHolder(View itemView) {
        super(itemView);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = itemView.getContext().getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);

        Drawable colorDrawable = new ColorDrawable(typedValue.data);

        StateListDrawable wrapper = new StateListDrawable();
        wrapper.addState(new int[]{android.R.attr.state_activated}, colorDrawable);
        wrapper.addState(StateSet.WILD_CARD, itemView.getBackground());

        mOriginalDrawable = itemView.getBackground();
        itemView.setBackgroundDrawable(wrapper);
    }

    public void setActivated(boolean isActivated) {
        itemView.setActivated(isActivated);
    }

    public boolean isActivated() {
        return itemView.isActivated();
    }

}
