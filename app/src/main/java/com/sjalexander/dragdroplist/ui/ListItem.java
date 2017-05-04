package com.sjalexander.dragdroplist.ui;

/**
 * Created by stephen.alexander on 24/04/2017.
 */

public class ListItem {
    int nameRes;
    int iconRes;

    public ListItem(int nameRes, int iconRes)
    {
        this.nameRes = nameRes;
        this.iconRes = iconRes;
    }

    public int getNameRes(){
        return nameRes;
    }

    public int getIconRes(){
        return iconRes;
    }
}
