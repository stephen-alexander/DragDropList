package com.sjalexander.dragdroplist.ui.listeners;

import android.view.View;
import android.widget.ListView;

public interface DragDropListener {

	void onDrag(int x, int y);

	void onDrop(View itemView);

}
