package com.bignerdranch.android.multiselector;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by Genius on 1/30/2015.
 */
public class WeakReferenceSelectableHolder<T extends  SelectableHolder> extends WeakReference implements Serializable{

    public WeakReferenceSelectableHolder(T r) {
        super(r);

    }
}
