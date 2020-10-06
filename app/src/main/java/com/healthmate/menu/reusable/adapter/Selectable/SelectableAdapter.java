package com.healthmate.menu.reusable.adapter.Selectable;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthmate.R;
import com.healthmate.menu.reusable.data.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectableAdapter extends RecyclerView.Adapter implements SelectableViewHolder.OnItemSelectedListener {
    private final List<SelectableItem> mValues;
    ArrayList<SelectableItem> arrayList;
    private boolean isMultiSelectionEnabled = false;
    SelectableViewHolder.OnItemSelectedListener listener;

    public SelectableAdapter(SelectableViewHolder.OnItemSelectedListener listener,
                             List<Item> items, boolean isMultiSelectionEnabled) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (Item item : items) {
            mValues.add(new SelectableItem(item, false));
        }
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(mValues);
    }

    @NonNull
    @Override
    public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selectable, parent, false);
        return new SelectableViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableItem selectableItem = mValues.get(position);
        String name = selectableItem.getName();
        holder.textView.setText(name);
        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }
        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArrayList<Item> getSelectedItems() {
        ArrayList<Item> selectedItems = new ArrayList<>();
        for (SelectableItem item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }

    public void search(String key) {
        key = key.toLowerCase(Locale.getDefault());
        mValues.clear();
        if (key.length() == 0) {
            mValues.addAll(arrayList);
        } else {
            for (SelectableItem wp : arrayList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(key)) {
                    mValues.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }


    @Override
    public void onItemSelected(SelectableItem item) {
        if (!isMultiSelectionEnabled) {
            for (SelectableItem selectableItem : mValues) {
                if (!selectableItem.equals(item) && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item) && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}
