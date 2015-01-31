package com.bignerdranch.android.multiselector;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class WeakHolderTracker implements Parcelable {
    private SparseArray<WeakReferenceSelectableHolder<SelectableHolder>> mHoldersByPosition =
            new SparseArray<>();

    /**
     * Returns the holder with a given position. If non-null, the returned
     * holder is guaranteed to have getPosition() == position.
     * @param position
     * @return
     */
    public SelectableHolder getHolder(int position) {
        WeakReference<SelectableHolder> holderRef = mHoldersByPosition.get(position);
        if (holderRef == null) {
            return null;
        }

        SelectableHolder holder = holderRef.get();
        if (holder == null || holder.getPosition() != position) {
            mHoldersByPosition.remove(position);
            return null;
        }

        return holder;
    }

    public void bindHolder(SelectableHolder holder, int position) {
        mHoldersByPosition.put(position, new WeakReferenceSelectableHolder<SelectableHolder>(holder));
    }

    public List<SelectableHolder> getTrackedHolders() {
        List<SelectableHolder> holders = new ArrayList<SelectableHolder>();

        for (int i = 0; i < mHoldersByPosition.size(); i++) {
            int key = mHoldersByPosition.keyAt(i);
            SelectableHolder holder = getHolder(key);

            if (holder != null) {
                holders.add(holder);
            }
        }

        return holders;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseArray((SparseArray)this.mHoldersByPosition);
    }

    public WeakHolderTracker() {
    }

    private WeakHolderTracker(Parcel in) {

        this.mHoldersByPosition = in.readSparseArray(SparseArray.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeakHolderTracker> CREATOR = new Parcelable.Creator<WeakHolderTracker>() {
        public WeakHolderTracker createFromParcel(Parcel source) {
            return new WeakHolderTracker(source);
        }

        public WeakHolderTracker[] newArray(int size) {
            return new WeakHolderTracker[size];
        }
    };
}
