package com.healthmate.menu.reusable.adapter.Selectable;


import com.healthmate.menu.reusable.data.Item;

public class SelectableItem extends Item {
    private boolean isSelected = false;

    public SelectableItem(Item item, boolean isSelected) {
        super(item.getName());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
