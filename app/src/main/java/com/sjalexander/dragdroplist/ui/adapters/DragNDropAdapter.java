/*
 * Copyright (C) 2010 Eric Harlow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjalexander.dragdroplist.ui.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjalexander.dragdroplist.R;
import com.sjalexander.dragdroplist.ui.ListItem;
import com.sjalexander.dragdroplist.ui.listeners.DropListener;
import com.sjalexander.dragdroplist.ui.listeners.RemoveListener;

public final class DragNDropAdapter extends BaseAdapter implements RemoveListener, DropListener {

	private LayoutInflater mInflater;
	
	private Activity activity;

	private int viewToHide = -1;
	/**
	 * The items held by the adaptor.
	 */
	protected static List<ListItem> items = new ArrayList<>();;

	public DragNDropAdapter(Activity activity, List<ListItem> items)
	{
		// Cache the LayoutInflate to avoid asking for a new one each time.
		this.activity = activity;
		mInflater = LayoutInflater.from(activity);
		this.items = items;
	}

	/**
	 * The number of items in the list
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		return items.size();
	}

	/**
	 * Since the data comes from an array, just returning the index is
	 * sufficient to get at the data. If we were using a more complex data
	 * structure, we would return whatever object represents one row in the
	 * list.
	 *
	 * @see android.widget.ListAdapter#getItem(int)
	 */
	public ListItem getItem(int position) {
		return items.get(position);
	}
	
	public List<ListItem> getItemList()
	{
		return items;
	}

	/**
	 * Use the array index as a unique id.
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}
	
	public void notifyDataSetChanged(int viewToHide)
	{
		this.viewToHide = viewToHide;
		super.notifyDataSetChanged();
	}

	/**
	 * Make a view to hold each row.
	 *
	 * @see android.widget.ListAdapter#getView(int, View,
	 *      ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// When convertView is not null, we can reuse it directly, there is no need
		// to reinflate it. We only inflate a new View when the convertView supplied
		// by ListView is null.

//		if (convertView == null)
//		{
			convertView = mInflater.inflate(R.layout.list_item, null);
			convertView.setBackgroundResource(R.drawable.selectable_background);
			ListItem item = (ListItem) getItem(position);

			ImageView icon = (ImageView) convertView.findViewById(R.id.cardIcon);

			icon.setImageResource(item.getIconRes());

			TextView serviceProviderLabel = (TextView) convertView.findViewById(R.id.serviceproviderName);
			serviceProviderLabel.setText(item.getNameRes());

//		} 
		//		else {
		//			// Get the ViewHolder back to get fast access to the TextView
		//			// and the ImageView.
		//			holder = (ViewHolder) convertView.getTag();
		//		}

		convertView.setTag(getItem(position).getNameRes());
		
		if (viewToHide == position)
			convertView.setVisibility(View.INVISIBLE);

		return convertView;
	}

	static class ViewHolder {
		TextView text;
	}

	public void onRemove(int which) {
		if (which < 0 || which > items.size()) return;
		items.remove(which);
	}

	public void onDrop(int from, int to) {
		ListItem temp = items.get(from);
		items.remove(from);
		items.add(to, temp);
	}
}