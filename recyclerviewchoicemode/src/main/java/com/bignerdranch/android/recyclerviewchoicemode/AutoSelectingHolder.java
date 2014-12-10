package com.bignerdranch.android.recyclerviewchoicemode;

import android.os.Build;
import android.support.v7.widget.RebindReportingHolder;
import android.view.View;

public abstract class AutoSelectingHolder extends RebindReportingHolder implements SelectableHolder {
    private final MultiSelector mMultiSelector;

    public AutoSelectingHolder(View itemView, MultiSelector multiSelector) {
        super(itemView);
        mMultiSelector = multiSelector;
    }

    @Override
    protected void onRebind() {
        mMultiSelector.bindHolder(this, getPosition(), getItemId());
    }
}
