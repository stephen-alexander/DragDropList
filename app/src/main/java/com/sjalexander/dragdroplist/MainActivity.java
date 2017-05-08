package com.sjalexander.dragdroplist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sjalexander.dragdroplist.ui.ListItem;
import com.sjalexander.dragdroplist.ui.adapters.DragDropAdapter;
import com.sjalexander.dragdroplist.ui.listeners.DragDropListener;
import com.sjalexander.dragdroplist.ui.views.DragDropListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DragDropListView cardList;
    private DragDropAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardList = (DragDropListView) findViewById(R.id.list_view);
        updateList();
    }

    private void updateList()
    {
        // Initialise list items
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(new ListItem(R.string.list_item_1, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.list_item_2, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.list_item_3, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.list_item_4, R.mipmap.ic_launcher));

        // Set up the list view adapter
        adapter = new DragDropAdapter(this, listItems);
        cardList.setAdapter(adapter);

        // Add listeners
        cardList.setDragDropListener(dragDropListener);
    }

    /**
     * Initialise drag listener listener
     */
    private DragDropListener dragDropListener =
            new DragDropListener() {

                public void onDrag(int from, int to) {
                    //Notify the adpter of the view's current location and which view to hide
                    adapter.onDrop(from, to);
                    adapter.notifyDataSetChanged();
                }

                public void onDrop(View itemView) {
                    // When the drag is complete, re-show the original list item view
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setPressed(false);
                }
            };
}
