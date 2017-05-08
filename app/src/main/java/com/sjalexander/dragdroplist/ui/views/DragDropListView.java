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

import com.sjalexander.dragdroplist.ui.listeners.DragDropListener;


public class DragDropListView extends ListView {

	private boolean isDrag = false;
    private int startListPos;
	private int currentListPos;
	private int touchOffset;
	private ImageView dragView;
	private DragDropListener dragDropListener;

    private WindowManager windowManager;

	public DragDropListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
	}

	public void setDragDropListener(DragDropListener dragDropListener) {
		this.dragDropListener = dragDropListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
        int y = (int) ev.getY();

        if (action == MotionEvent.ACTION_DOWN)
        {
			isDrag = true;
            startListPos = pointToPosition(0,y);
		}

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
			performDrag(y);
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		default:
			isDrag = false;
			if (dragDropListener != null)
				dragDropListener.onDrop(getChildAt(currentListPos));

            removeDragView();

			break;
		}
		return true;
	}

    // enable the drag view for dragging
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

	// move the drag view
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
			if (dragDropListener != null && nextListPos != INVALID_POSITION)
			{
                // Notify the listener
                if (dragDropListener != null)
					dragDropListener.onDrag(currentListPos, nextListPos);

				currentListPos = nextListPos;
			}
		}
	}

	// destroy performDrag view
	private void removeDragView() {
		if (dragView != null) {
            dragView.setVisibility(INVISIBLE);
			windowManager.removeView(dragView);
			dragView.setImageDrawable(null);
			dragView = null;
		}
	}
}
