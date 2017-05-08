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

public final class DragDropAdapter extends BaseAdapter {

	/**
	 * The layout inflater
	 */
	private LayoutInflater mInflater;

	/**
	 * The view to hide when in drag mode
	 */
	private int viewToHide = -1;

    private int from = -1;
    private int to = -1;

	/**
	 * The items held by the adaptor.
	 */
	protected static List<ListItem> items = new ArrayList<>();;

	public DragDropAdapter(Activity activity, List<ListItem> items)
	{
		mInflater = LayoutInflater.from(activity);
		this.items = items;
	}

	public int getCount() {
		return items.size();
	}

	public ListItem getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	private void setViewToHide(int viewToHide)
	{
		this.viewToHide = viewToHide;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
        convertView = mInflater.inflate(R.layout.list_item, null);
        convertView.setBackgroundResource(R.drawable.selectable_background);
        ListItem item = (ListItem) getItem(position);

        ImageView icon = (ImageView) convertView.findViewById(R.id.cardIcon);

        icon.setImageResource(item.getIconRes());

        TextView serviceProviderLabel = (TextView) convertView.findViewById(R.id.serviceproviderName);
        serviceProviderLabel.setText(item.getNameRes());

        convertView.setTag(getItem(position).getNameRes());

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

	public void onDrop(int from, int to) {
        // Reorder the list
		ListItem temp = items.get(from);
		items.remove(from);
		items.add(to, temp);

        this.viewToHide = to;
	}
}