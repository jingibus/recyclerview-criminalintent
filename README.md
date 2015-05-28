RecyclerView Multiselect
========================

RecyclerView MultiSelect is a tool to help implement single or multichoice selection on RecyclerView items. It does not provide the same interface as ListView's setChoiceMode(), but can do everything setChoiceMode() does. It is also small with a limited set of responsibilities, which means it should be suitable for coercing into off label uses.

To import, add the following line to your build.gradle:

    compile 'com.bignerdranch.android:recyclerview-multiselect:+'

You can find Javadocs [here](javadocs/).

## Basics

First, create a MultiSelector instance. 

    public class CrimeListFragment extends Fragment {

        private MultiSelector mMultiSelector = new MultiSelector();

        ...
    }

The MultiSelector knows which items are selected, and is also your interface for controlling item selection across everything it is hooked up to. In this case, that's everything in a RecyclerView adapter.

MultiSelector works by talking to ViewHolders. We provide a ViewHolder subclass called SwappingHolder that provides out-of-the-box selection visualization. To hook up a SwappingHolder to a MultiSelector, pass in the MultiSelector in the constructor, and use click listeners to call through to MultiSelector.tapSelection():

    private class MyHolder extends SwappingHolder
            implements View.OnClickListener {
        private final CheckBox mSolvedCheckBox;

        public MyHolder(View itemView) {
            super(itemView, mMultiSelector);

            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.solvedCheckBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMultiSelector.tapSelection(this)) {
                // Selection is on, so tapSelection() toggled item selection.
            } else {
                // Selection is off; handle normal item click here.
            }
        }
    }

MultiSelector.tapSelection() simulates tapping a selected item; if the MultiSelector is in selection mode, it returns true and toggles the selection for that item. If not, it returns false and does nothing.

Initially, selection is off. To turn on multiselect mode, call setSelectable(true):

    mMultiSelector.setSelectable(true);

This will toggle the selectable flag on the MultiSelector, and toggle it on all its bound SwappingHolders, too. This is all done for you by SwappingHolder — it extends MultiSelectorBindingHolder, which binds itself to your MultiSelector.

And for basic multiselect, that's all there is to it. When you need to know whether an item is selected or not, ask the multiselector:

    for (int i = mCrimes.size(); i > 0; i--) {
        if (mMultiSelector.isSelected(i, 0)) {
            Crime crime = mCrimes.get(i);
            CrimeLab.get(getActivity()).deleteCrime(crime);
            mRecyclerView.getAdapter().notifyItemRemoved(i);
        }
    }

## Single selection

To use single selection instead of multiselect, use SingleSelector instead of MultiSelector:

    public class CrimeListFragment extends Fragment {

        private MultiSelector mMultiSelector = new SingleSelector();

        ...
    }

## Modal multiselection with long click

To get the same effect as CHOICE\_MODE\_MULTIPLE\_MODAL, you can either write your own ActionMode.Callback that calls MultiSelector.setSelectable() to turn selection on and off, or use the provided abstract implementation, ModalMultiSelectorCallback:

    private ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, Menu menu);
	     getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_item_delete_crime:
                    // Delete crimes from model

                    mMultiSelector.clearSelections();
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

ModalMultiSelectorCallback will call MultiSelector.setSelectable(true) and clearSelections() inside onPrepareActionMode, and setSelectable(false) in onDestroyActionMode. Kick it off like any other action mode inside a long click listener:

    private class MyHolder extends SwappingHolder
            implements View.OnClickListener, View.OnLongClickListener {
        public MyHolder(View itemView) {
            ...

            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
        }

        @Override
        public boolean onLongClick(View v) {
            ActionBarActivity activity = (ActionBarActivity)getActivity();
            activity.startSupportActionMode(mDeleteMode);
            mMultiSelector.setSelected(this, true);
            return true;
        }
    }

## Customizing selection visuals 

SwappingDrawable uses two sets of drawables and state list animators for its itemView: one while in the default mode, and one while in selection mode. You can customize these by calling one of the various setters:

    public void setSelectionModeBackgroundDrawable(Drawable drawable);
    public void setDefaultModeBackgroundDrawable(Drawable drawable);

    public void setSelectionModeStateListAnimator(int resId);
    public void setDefaultModeStateListAnimator(int resId);

The state list animator setters are safe to call prior to API 21, and will result in a no-op.

### Off label customization

If you need to customize what the selected states look like beyond what SwappingHolder offers, you can extend the MultiSelectorBindingHolder abstract class to respond to selection events:

    public class MyCustomHolder extends MultiSelectorBindingHolder {
        @Override
        public void setSelectable(boolean selectable) { ... }

        @Override
        public boolean isSelectable() { ... }

        @Override
        public void setActivated(boolean activated) { ... }

        @Override
        public boolean isActivated() { ... }
    }

If that's still too restrictive, you can implement the SelectableHolder interface instead:

    public interface SelectableHolder {
        void setSelectable(boolean selectable);
        boolean isSelectable();

        void setActivated(boolean activated);
        boolean isActivated();

        int getAdapterPosition();
        long getItemId();
    }

It requires a bit more code: you will need to bind your ViewHolder to the MultiSelector by calling MultiSelector.bindHolder() every time your view holder is bound to a different item. On the other hand, though, your implementation doesn't even necessarily have to be hooked up to a RecyclerView, and neither does your MultiSelector. I have no idea how useful that is to anyone, but there you go.
