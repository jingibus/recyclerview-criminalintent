package com.bignerdranch.android.multiselector;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;

/**
 * <p>
 * A holder extended to support having a selectable mode with a different
 * background and state list animator.</p>
 * <p>When {@link #setSelectable(boolean)} is set to true, itemView's
 * background is set to the value of selectionModeBackgroundDrawable,
 * and its StateListAnimator is set to selectionModeStateListAnimator.
 * When it is set to false, the defaultModeBackgroundDrawable and
 * defaultModeStateListAnimator are used.</p>
 * <p>defaultModeBackgroundDrawable and defaultModeStateListAnimator
 * default to the values on itemView at the time the holder was constructed.</p>
 * <p>selectionModeBackgroundDrawable defaults to a StateListDrawable that displays
 * your colorAccent theme color when state_activated=true, and nothing otherwise.
 * selectionModeStateListAnimator defaults to a raise animation that animates selection
 * items to a 12dp translationZ.</p>
 * <p>(Thanks to <a href="https://github.com/kurtisnelson/">Kurt Nelson</a> for examples and discussion on approaches here.)</p>
 */
public  class SwappingHolder extends MultiSelectorBindingHolder implements SelectableHolder {
    private MultiSelector mMultiSelector;
    private boolean mIsSelectable = false;
    private Drawable mSelectionModeBackgroundDrawable;
    private Drawable mDefaultModeBackgroundDrawable;
    private StateListAnimator mSelectionModeStateListAnimator;
    private StateListAnimator mDefaultModeStateListAnimator;

    /**
     * <p>Construct a new SelectableHolder hooked up to be controlled by a MultiSelector.</p>
     * <p>If the MultiSelector is not null, the SelectableHolder can be selected by
     * calling {@link com.bignerdranch.android.multiselector.MultiSelector#setSelected(com.bignerdranch.android.multiselector.SelectableHolder, boolean)}.</p>
     * <p>If the MultiSelector is null, the SelectableHolder acts as a standalone
     * ViewHolder that you can control manually by setting {@link #setSelectable(boolean)}
     * and {@link #setActivated(boolean)}</p>
     *
     * @param itemView      Item view for this ViewHolder
     * @param multiSelector A selector set to bind this holder to
     */
    public SwappingHolder(View itemView, MultiSelector multiSelector) {
        super(itemView, multiSelector);

        mMultiSelector = multiSelector;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSelectionModeStateListAnimator(getRaiseStateListAnimator(itemView.getContext()));
            setDefaultModeStateListAnimator(itemView.getStateListAnimator());
        }
        // Default selection mode background drawable is this
        setSelectionModeBackgroundDrawable(
                getAccentStateDrawable(itemView.getContext()));
        setDefaultModeBackgroundDrawable(
                itemView.getBackground());

    }

    /**
     * <p>Construct a new standalone SelectableHolder.</p>
     * <p>Selectable state can be controlled manually by setting {@link #setSelectable(boolean)}.</p>
     *
     * @param itemView Item view for this ViewHolder
     */
    public SwappingHolder(View itemView) {
        this(itemView, null);
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

    private static StateListAnimator getRaiseStateListAnimator(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return AnimatorInflater.loadStateListAnimator(context, R.anim.raise);
        } else {
            return null;
        }
    }

    /**
     * <p>Background drawable to use in selection mode. This defaults to
     * a state list drawable that uses the colorAccent theme value when
     * <code>state_activated==true</code>.</p>
     *
     * @return A background drawable
     */
    public Drawable getSelectionModeBackgroundDrawable() {
        return mSelectionModeBackgroundDrawable;
    }

    /**
     * <p>Set the background drawable to be used in selection mode.</p>
     *
     * @param selectionModeBackgroundDrawable A background drawable
     */
    public void setSelectionModeBackgroundDrawable(Drawable selectionModeBackgroundDrawable) {
        mSelectionModeBackgroundDrawable = selectionModeBackgroundDrawable;

        if (mIsSelectable) {
            itemView.setBackgroundDrawable(selectionModeBackgroundDrawable);
        }
    }

    /**
     * <p>Background drawable to use when not in selection mode. This defaults
     * to the drawable that was set on {@link #itemView} at construction time.</p>
     *
     * @return A background drawable
     */
    public Drawable getDefaultModeBackgroundDrawable() {
        return mDefaultModeBackgroundDrawable;
    }

    /**
     * <p>Set the background drawable to use when not in selection mode.</p>
     *
     * @param defaultModeBackgroundDrawable A background drawable
     */
    public void setDefaultModeBackgroundDrawable(Drawable defaultModeBackgroundDrawable) {
        mDefaultModeBackgroundDrawable = defaultModeBackgroundDrawable;

        if (!mIsSelectable) {
            itemView.setBackgroundDrawable(mDefaultModeBackgroundDrawable);
        }
    }

    /**
     * <p>State list animator to use when in selection mode. This defaults
     * to an animator that raises the view when <code>state_activated==true</code>.</p>
     *
     * @return A state list animator
     */
    public StateListAnimator getSelectionModeStateListAnimator() {
        return mSelectionModeStateListAnimator;
    }

    /**
     * Set the state list animator to use when in selection mode.
     *
     * @param selectionModeStateListAnimator A state list animator
     */
    public void setSelectionModeStateListAnimator(StateListAnimator selectionModeStateListAnimator) {
        mSelectionModeStateListAnimator = selectionModeStateListAnimator;
    }

    /**
     * <p>Set the state list animator to use when in selection mode. If not run
     * on a Lollipop device, this method is a no-op.</p>
     *
     * @param resId A state list animator resource id. Ignored prior to Lollipop.
     */
    public void setSelectionModeStateListAnimator(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator =
                    AnimatorInflater.loadStateListAnimator(itemView.getContext(), resId);

            setSelectionModeStateListAnimator(animator);
        }
    }

    /**
     * Get the state list animator to use when not in selection mode.
     * This value defaults to the animator set on {@link #itemView} at
     * construction time.
     *
     * @return A state list animator
     */
    public StateListAnimator getDefaultModeStateListAnimator() {
        return mDefaultModeStateListAnimator;
    }

    /**
     * Set the state list animator to use when in default mode. If not run
     * on a Lollipop device, this method is a no-op.
     *
     * @param resId A state list animator resource id. Ignored prior to Lollipop.
     */
    public void setDefaultModeStateListAnimator(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator =
                    AnimatorInflater.loadStateListAnimator(itemView.getContext(), resId);

            setDefaultModeStateListAnimator(animator);
        }

    }

    /**
     * Set the state list animator to use when not in selection mode.
     *
     * @param defaultModeStateListAnimator A state list animator
     */
    public void setDefaultModeStateListAnimator(StateListAnimator defaultModeStateListAnimator) {
        mDefaultModeStateListAnimator = defaultModeStateListAnimator;
    }

    /**
     * Whether this holder is currently activated/selected.
     * Calls through to {@link View#setActivated(boolean)} on {@link #itemView}.
     *
     * @return True if the view is activated.
     */
    public boolean isActivated() {
        return itemView.isActivated();
    }

    /**
     * Activate/select this holder.
     * Calls through to {@link android.view.View#isActivated()} on {@link #itemView}.
     *
     * @param isActivated True to activate the view.
     */
    public void setActivated(boolean isActivated) {
        itemView.setActivated(isActivated);
    }

    /**
     * Returns whether {@link #itemView} is currently in a
     * selectable mode.
     *
     * @return True if selectable.
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
     * @param isSelectable True if selectable.
     */
    public void setSelectable(boolean isSelectable) {
        boolean changed = isSelectable != mIsSelectable;
        mIsSelectable = isSelectable;

        if (changed) {
            refreshChrome();
        }
    }

    private void refreshChrome() {
        Drawable backgroundDrawable = mIsSelectable ? mSelectionModeBackgroundDrawable
                : mDefaultModeBackgroundDrawable;
        itemView.setBackgroundDrawable(backgroundDrawable);
        if (backgroundDrawable != null) {
            backgroundDrawable.jumpToCurrentState();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator = mIsSelectable ? mSelectionModeStateListAnimator
                    : mDefaultModeStateListAnimator;

            itemView.setStateListAnimator(animator);
            if (animator != null) {
                animator.jumpToCurrentState();
            }
        }
    }


}
