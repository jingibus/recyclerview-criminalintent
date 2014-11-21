package com.bignerdranch.android.criminalintent;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * A holder extended to support having a selectable mode with a different
 * background and state list animator.
 *
 * When {@link #setSelectable(boolean)} is set to true, itemView's
 * background is set to the value of selectionModeBackgroundDrawable,
 * and its StateListAnimator is set to selectionModeStateListAnimator.
 * When it is set to false, the defaultModeBackgroundDrawable and
 * defaultModeStateListAnimator are used.
 *
 * defaultModeBackgroundDrawable and defaultModeStateListAnimator
 * default to the values on itemView at the time the holder was constructed.
 *
 * selectionModeBackgroundDrawable defaults to the value of ?android:attr:listSelector,
 * the same as is used by ListView's selection mode.
 * selectionModeStateListAnimator default to null.
 */
public abstract class SelectableHolder extends RecyclerView.ViewHolder {
    private boolean mIsSelectable = false;

    private Drawable mSelectionModeBackgroundDrawable;
    private Drawable mDefaultModeBackgroundDrawable;
    private StateListAnimator mSelectionModeStateListAnimator;
    private StateListAnimator mDefaultModeStateListAnimator;

    private static Drawable getThemeResourceDrawable(Context context, int themeAttr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(themeAttr, typedValue, true);
        int drawableResId = typedValue.resourceId;

        return context.getResources().getDrawable(drawableResId);
    }

    public SelectableHolder(View itemView) {
        super(itemView);

        // Default selection mode background drawable is this
        setSelectionModeBackgroundDrawable(
                getThemeResourceDrawable(itemView.getContext(), android.R.attr.activatedBackgroundIndicator));
        setDefaultModeBackgroundDrawable(
                itemView.getBackground());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSelectionModeStateListAnimator(null);
            setDefaultModeStateListAnimator(itemView.getStateListAnimator());
        }
    }

    public void setActivated(boolean isActivated) {
        itemView.setActivated(isActivated);
    }

    public boolean isActivated() {
        return itemView.isActivated();
    }

    public boolean isSelectable() {
        return mIsSelectable;
    }

    public void setSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;

        refreshChrome();
    }

    private void refreshChrome() {
        if (mIsSelectable) {
            itemView.setBackgroundDrawable(mSelectionModeBackgroundDrawable);
        } else {
            itemView.setBackgroundDrawable(mDefaultModeBackgroundDrawable);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mIsSelectable) {
                itemView.setStateListAnimator(mSelectionModeStateListAnimator);
            } else {
                itemView.setStateListAnimator(mDefaultModeStateListAnimator);
            }
        }
    }

    public Drawable getSelectionModeBackgroundDrawable() {
        return mSelectionModeBackgroundDrawable;
    }

    public void setSelectionModeBackgroundDrawable(Drawable selectionModeBackgroundDrawable) {
        mSelectionModeBackgroundDrawable = selectionModeBackgroundDrawable;

        if (mIsSelectable) {
            itemView.setBackgroundDrawable(selectionModeBackgroundDrawable);
        }
    }

    public Drawable getDefaultModeBackgroundDrawable() {
        return mDefaultModeBackgroundDrawable;
    }

    public void setDefaultModeBackgroundDrawable(Drawable defaultModeBackgroundDrawable) {
        mDefaultModeBackgroundDrawable = defaultModeBackgroundDrawable;

        if (!mIsSelectable) {
            itemView.setBackgroundDrawable(mDefaultModeBackgroundDrawable);
        }
    }

    public StateListAnimator getSelectionModeStateListAnimator() {
        return mSelectionModeStateListAnimator;
    }

    public void setSelectionModeStateListAnimatorResource(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator =
                AnimatorInflater.loadStateListAnimator(itemView.getContext(), resId);

            setSelectionModeStateListAnimator(animator);
        }
    }

    public void setSelectionModeStateListAnimator(StateListAnimator selectionModeStateListAnimator) {
        mSelectionModeStateListAnimator = selectionModeStateListAnimator;
    }

    public StateListAnimator getDefaultModeStateListAnimator() {
        return mDefaultModeStateListAnimator;
    }

    public void setDefaultModeStateListAnimator(StateListAnimator defaultModeStateListAnimator) {
        mDefaultModeStateListAnimator = defaultModeStateListAnimator;
    }
}
