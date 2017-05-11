/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */
package com.sjalexander.dragdroplist.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjalexander.dragdroplist.R;
import com.sjalexander.dragdroplist.ui.ListItem;

/**
 * Drag and Drop list view adapter
 */
public final class DragDropAdapter extends BaseAdapter {

	/**
	 * The layout inflater
	 */
	private LayoutInflater mInflater;

	/**
	 * The view to hide when in drag mode
	 */
	private int viewToHide = -1;

	/**
	 * The items held by the adaptor.
	 */
	protected static List<ListItem> items = new ArrayList<>();;

    /**
     * Constuctor
     *
     * @param activity The current activity
     * @param items The items to be displayed
     */
	public DragDropAdapter(Activity activity, List<ListItem> items)
	{
		mInflater = LayoutInflater.from(activity);
		this.items = items;
	}

    /**
     * Get the number of items
     *
     * @return the size of the items list
     */
	public int getCount() {
		return items.size();
	}

    /**
     * Get an item from the list
     *
     * @param position The position of the item
     * @return The item at the position
     */
	public ListItem getItem(int position) {
		return items.get(position);
	}

    /**
     * Get the item ID
     *
     * @param position The position of the item
     * @return The item ID
     */
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
        //Create the view
        convertView = mInflater.inflate(R.layout.list_item, null);
        convertView.setBackgroundResource(R.drawable.selectable_background);
        ListItem item = getItem(position);

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        icon.setImageResource(item.getIconRes());

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(item.getNameRes());

		// Check if we are to hide the view
		if (viewToHide == position)
		{
            convertView.setVisibility(View.INVISIBLE);
        }
        // If not, check if the view is already hidden,
        // If so, make the view visible
        else if (convertView.getVisibility() == View.INVISIBLE)
        {
            convertView.setVisibility(View.VISIBLE);
        }

		return convertView;
	}

    /**
     * Call to notify the adapter a list item had been dragged into another position
     * @param from The items original position
     * @param to The items new position
     */
	public void onDrag(int from, int to) {
        // Reorder the list
		ListItem temp = items.get(from);
		items.remove(from);
		items.add(to, temp);

        // Set which list position to be hidden and notify of the change
        this.viewToHide = to;
		notifyDataSetChanged();
	}

    /**
     * Call to notify the view has been dropped
     */
	public void onDrop()
	{
        // clear the view to be hidden and notify of the change
		viewToHide = -1;
		notifyDataSetChanged();
	}
}