package android.support.v7.widget;

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

import com.bignerdranch.android.recyclerviewchoicemode.Multiselector;
import com.bignerdranch.android.recyclerviewchoicemode.R;

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

    private Multiselector mMultiselector;

    private Drawable mSelectionModeBackgroundDrawable;
    private Drawable mDefaultModeBackgroundDrawable;
    private StateListAnimator mSelectionModeStateListAnimator;
    private StateListAnimator mDefaultModeStateListAnimator;

    /**
     * Construct a new SelectableHolder hooked up to be controlled by a Multiselector.
     *
     * If the Multiselector is not null, the SelectableHolder can be selected by
     * calling {@link com.bignerdranch.android.recyclerviewchoicemode.Multiselector#setSelected(SelectableHolder, boolean)}.
     *
     * If the Multiselector is null, the SelectableHolder acts as a standalone
     * ViewHolder that you can control manually by setting {@link #setSelectable(boolean)}
     * and {@link #setActivated(boolean)}
     *
     * @param itemView Item view for this ViewHolder
     * @param multiselector
     */
    public SelectableHolder(View itemView, Multiselector multiselector) {
        super(itemView);
        mMultiselector = multiselector;

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
    /**
     * Construct a new standalone SelectableHolder.
     *
     * Selectable state can be controlled manually by setting {@link #setSelectable(boolean)}.
     *
     * and {@link #setActivated(boolean)}
     *
     * @param itemView Item view for this ViewHolder
     */
    public SelectableHolder(View itemView) {
        this(itemView, null);
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
    /**
     * Overridden to detect changing positions. RecyclerView often uses flags to
     * signal that a holder has been rebound.
     *
     * @param flags
     * @param mask
     */
    @Override
    void setFlags(int flags, int mask) {
        super.setFlags(flags, mask);
        int setFlags = mask & flags;
        checkFlags(setFlags);
    }

    /**
     * Overridden to detect changing positions. RecyclerView often uses flags to
     * signal that a holder has been rebound.
     *
     * @param flags
     */
    @Override
    void addFlags(int flags) {
        super.addFlags(flags);
        checkFlags(flags);
    }

    private void checkFlags(int setFlags) {
        if (mMultiselector != null && isRelevantFlagSet(setFlags)) {
            mMultiselector.bindHolder(this, getPosition(), getItemId());
        }
    }

    private static boolean isRelevantFlagSet(int flag) {
        for (Integer value : new int[] { FLAG_BOUND, FLAG_CHANGED, FLAG_UPDATE, FLAG_RETURNED_FROM_SCRAP }) {
            if ((flag & value) == value) {
                return true;
            }
        }

        return false;
    }

    /**
     * Overridden to update this holder's binding in the multiselector.
     * @param offset
     * @param applyToPreLayout
     */
    @Override
    void offsetPosition(int offset, boolean applyToPreLayout) {
        super.offsetPosition(offset, applyToPreLayout);
        mMultiselector.bindHolder(this, getPosition(), getItemId());
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

}
