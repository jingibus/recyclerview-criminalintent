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
 * When {@link #setSelectable(boolean)} is set to true, itemView's
 * background is set to the value of selectionModeBackgroundDrawable,
 * and its StateListAnimator is set to selectionModeStateListAnimator.
 * When it is set to false, the defaultModeBackgroundDrawable and
 * defaultModeStateListAnimator are used.
 *
 * defaultModeBackgroundDrawable and defaultModeStateListAnimator
 * default to the values on itemView at the time the holder was constructed.
 *
 * selectionModeBackgroundDrawable defaults to a StateListDrawable that displays
 * your colorAccent theme color when state_activated=true, and nothing otherwise.
 * selectionModeStateListAnimator defaults to a raise animation that animates selection
 * items to a 12dp translationZ.
 *
 * (Thanks to Kurt Nelson for examples and discussion on approaches here.
 * @see <a href="https://github.com/kurtisnelson/">https://github.com/kurtisnelson/</a>)
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

    private static Drawable getAccentStateDrawable(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);

        Drawable colorDrawable = new ColorDrawable(typedValue.data);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_activated}, colorDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, null);

        return stateListDrawable;
    }

    public SelectableHolder(View itemView) {
        super(itemView);

        // Default selection mode background drawable is this
        setSelectionModeBackgroundDrawable(
                getAccentStateDrawable(itemView.getContext()));
        setDefaultModeBackgroundDrawable(
                itemView.getBackground());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSelectionModeStateListAnimator(getRaiseStateListAnimator(itemView.getContext()));
            setDefaultModeStateListAnimator(itemView.getStateListAnimator());
        }
    }

    private StateListAnimator getRaiseStateListAnimator(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return AnimatorInflater.loadStateListAnimator(context, R.anim.raise);
        } else {
            return null;
        }
    }

    /**
     * Calls through to {@link #itemView#setActivated}.
     *
     * @param isActivated
     */
    public void setActivated(boolean isActivated) {
        itemView.setActivated(isActivated);
    }

    /**
     * Calls through to {@link #itemView#isActivated}.
     * @return
     */
    public boolean isActivated() {
        return itemView.isActivated();
    }

    /**
     * Returns whether {@link #itemView} is currently in a
     * selectable mode.
     *
     * @return
     */
    public boolean isSelectable() {
        return mIsSelectable;
    }

    /**
     * Turns selection mode on and off. When in selection mode,
     * {@link #itemView}'s background drawable is swapped out
     * for the value of {@link #getSelectionModeBackgroundDrawable()}.
     * When not, it is set to {@link #getDefaultModeBackgroundDrawable()}.
     * If in Lollipop or greater versions, the same applies to
     * {@link #getSelectionModeStateListAnimator()} and
     * {@link #getDefaultModeStateListAnimator()}.
     *
     * @param isSelectable
     */
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

    /**
     * Background drawable to use in selection mode. This defaults to
     * a state list drawable that uses the colorAccent theme value when
     * <code>state_activated==true</code>.
     * @return
     */
    public Drawable getSelectionModeBackgroundDrawable() {
        return mSelectionModeBackgroundDrawable;
    }

    /**
     * Set the background drawable to be used in selection mode.
     * @param selectionModeBackgroundDrawable
     */
    public void setSelectionModeBackgroundDrawable(Drawable selectionModeBackgroundDrawable) {
        mSelectionModeBackgroundDrawable = selectionModeBackgroundDrawable;

        if (mIsSelectable) {
            itemView.setBackgroundDrawable(selectionModeBackgroundDrawable);
        }
    }

    /**
     * Background drawable to use when not in selection mode. This defaults
     * to the drawable that was set on {@link #itemView} at construction time.
     * @return
     */
    public Drawable getDefaultModeBackgroundDrawable() {
        return mDefaultModeBackgroundDrawable;
    }

    /**
     * Set the background drawable to use when not in selection mode.
     * @param defaultModeBackgroundDrawable
     */
    public void setDefaultModeBackgroundDrawable(Drawable defaultModeBackgroundDrawable) {
        mDefaultModeBackgroundDrawable = defaultModeBackgroundDrawable;

        if (!mIsSelectable) {
            itemView.setBackgroundDrawable(mDefaultModeBackgroundDrawable);
        }
    }

    /**
     * State list animator to use when in selection mode. This defaults
     * to an animator that raises the view when <code>state_activated==true</code>.
     * @return
     */
    public StateListAnimator getSelectionModeStateListAnimator() {
        return mSelectionModeStateListAnimator;
    }

    /**
     * Set the state list animator to use when in selection mode. If not run
     * on a Lollipop device, this method is a no-op.
     * @param resId
     */
    public void setSelectionModeStateListAnimatorResource(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator =
                AnimatorInflater.loadStateListAnimator(itemView.getContext(), resId);

            setSelectionModeStateListAnimator(animator);
        }
    }

    /**
     * Set the state list animator to use when in selection mode.
     * @param selectionModeStateListAnimator
     */
    public void setSelectionModeStateListAnimator(StateListAnimator selectionModeStateListAnimator) {
        mSelectionModeStateListAnimator = selectionModeStateListAnimator;
    }

    /**
     * Get the state list animator to use when not in selection mode.
     * This value defaults to the animator set on {@link #itemView} at
     * construction time.
     * @return
     */
    public StateListAnimator getDefaultModeStateListAnimator() {
        return mDefaultModeStateListAnimator;
    }

    /**
     * Set the state list animator to use when not in selection mode.
     *
     * @param defaultModeStateListAnimator
     */
    public void setDefaultModeStateListAnimator(StateListAnimator defaultModeStateListAnimator) {
        mDefaultModeStateListAnimator = defaultModeStateListAnimator;
    }
}
