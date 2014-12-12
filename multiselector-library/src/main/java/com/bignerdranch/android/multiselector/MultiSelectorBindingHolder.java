package com.bignerdranch.android.multiselector;

import android.support.v7.widget.RebindReportingHolder;
import android.view.View;

/**
 * A {@link android.support.v7.widget.RecyclerView.ViewHolder} that will automatically
 * bind itself to items in a {@link com.bignerdranch.android.multiselector.MultiSelector}.
 * This is like a {@link com.bignerdranch.android.multiselector.SwappingHolder}, but without
 * any background swapping, so if you want to implement {@link com.bignerdranch.android.multiselector.SelectableHolder},
 * this is usually the best place to start.
 */
public abstract class MultiSelectorBindingHolder extends RebindReportingHolder implements SelectableHolder {
    private final MultiSelector mMultiSelector;

    public MultiSelectorBindingHolder(View itemView, MultiSelector multiSelector) {
        super(itemView);
        mMultiSelector = multiSelector;
    }

    @Override
    protected void onRebind() {
        mMultiSelector.bindHolder(this, getPosition(), getItemId());
    }
}
