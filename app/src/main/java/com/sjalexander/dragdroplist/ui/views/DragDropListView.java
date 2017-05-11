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
package com.sjalexander.dragdroplist.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.sjalexander.dragdroplist.ui.adapters.DragDropAdapter;

/**
 * Drag and Drop list view
 */
public class DragDropListView extends ListView {

    /* Is currently dragging flag */
	private boolean isDrag = false;

    /* List position integers */
    private int startListPos;
	private int currentListPos;

    /* The offset used to calculate where to draw the drag view */
	private int touchOffset;

    /* The drag view */
	private ImageView dragView;

    /* Drag and Drop adapter */
	private DragDropAdapter adapter;

    /* window manager */
    private WindowManager windowManager;

    /**
     * Drag and Drop list view constructor
     * @param context The activity context
     * @param attrs The view Attribute Set
     */
	public DragDropListView(Context context, AttributeSet attrs) {
		super(context, attrs);

        // Get the window manager here so we don't have to keep getting it
        windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
	}

    /**
     * Set the Drag and Drop adapter for the viw
     * @param adapter The DragDropAdapter
     */
	public void setAdapter(DragDropAdapter adapter){
        // Ensure the super is called
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
        int y = (int) ev.getY();

        if (action == MotionEvent.ACTION_DOWN)
        {
            // Start dragging
			isDrag = true;
            startListPos = pointToPosition(0,y);
		}

		// If we're not dragging and have pressed an
        // invalid position pass the action on
		if (!isDrag || startListPos == INVALID_POSITION)
        {
            return super.onTouchEvent(ev);
        }
        
		switch (action)
        {
		case MotionEvent.ACTION_DOWN:
		    // Set the current list position of the item
            currentListPos = startListPos;

            // Calculate the Y offset of the touch from the view top
            touchOffset = y - getChildAt(currentListPos).getTop();
            touchOffset -= ((int)ev.getRawY()) - y;

            // Prepare for performDrag
            initialiseDragView(currentListPos, y);
			break;

		case MotionEvent.ACTION_MOVE:
		    // drag view
			performDrag(y);
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		default:
		    // cancel drag
			isDrag = false;
			if (adapter != null)
				adapter.onDrop();

            removeDragView();

			break;
		}
		return true;
	}

    /**
     * Create the view to be dragged
     *
     * @param itemIndex The index of the view
     * @param y The Y position to draw the view
     */
    private void initialiseDragView(int itemIndex, int y)
    {
        // Get the view to be dragged
        View item = getChildAt(itemIndex);
        item.setPressed(true);
        item.setDrawingCacheEnabled(true);

        // Create a bitmap copy of the view
        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());

        // Set up the layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Set the initial view position, taking into account the Y touch offset
        layoutParams.x = 0;
        layoutParams.y = y - touchOffset;
        layoutParams.gravity = Gravity.TOP;

        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.windowAnimations = 0;

        // Create a view containing the bitmap
        Context context = getContext();
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);

        // Add the view to the window
        windowManager.addView(imageView, layoutParams);
        dragView = imageView;
    }

    /**
     * Move the drag view
     *
     * @param y The current Y position
     */
	private void performDrag(int y) {
		if (dragView != null)
		{
            // Update Y position of view
			WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) dragView.getLayoutParams();
			layoutParams.y = y - touchOffset;
            layoutParams.x = 0;

			windowManager.updateViewLayout(dragView, layoutParams);

            // Calculate next list position of item
			int nextListPos = pointToPosition(0,y);
			if (adapter != null && nextListPos != INVALID_POSITION)
			{
                // Notify the adapter
                if (adapter != null)
                    adapter.onDrag(currentListPos, nextListPos);

				currentListPos = nextListPos;
			}
		}
	}

    /**
     * Clean up the drag view
     */
	private void removeDragView() {
		if (dragView != null) {
            dragView.setVisibility(INVISIBLE);
			windowManager.removeView(dragView);
			dragView.setImageDrawable(null);
			dragView = null;
		}
	}
}
