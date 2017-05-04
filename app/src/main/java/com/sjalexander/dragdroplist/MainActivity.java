package com.sjalexander.dragdroplist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sjalexander.dragdroplist.ui.ListItem;
import com.sjalexander.dragdroplist.ui.adapters.DragNDropAdapter;
import com.sjalexander.dragdroplist.ui.listeners.DragListener;
import com.sjalexander.dragdroplist.ui.listeners.DropListener;
import com.sjalexander.dragdroplist.ui.listeners.RemoveListener;
import com.sjalexander.dragdroplist.ui.views.DragDropListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DragDropListView cardList;
    private DragNDropAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardList = (DragDropListView) findViewById(R.id.list_view);

        updateList();
    }

    private void updateList()
    {
        List<ListItem> listItems = new ArrayList<>();

        listItems.add(new ListItem(R.string.app_name, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.app_name, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.app_name, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.app_name, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.app_name, R.mipmap.ic_launcher));
        listItems.add(new ListItem(R.string.app_name, R.mipmap.ic_launcher));

        if (!listItems.isEmpty())
        {
            adapter = new DragNDropAdapter(this, listItems);
            cardList.setAdapter(adapter);

            ((DragDropListView) cardList).setDropListener(mDropListener);
            ((DragDropListView) cardList).setRemoveListener(mRemoveListener);
            ((DragDropListView) cardList).setDragListener(mDragListener);
        }
    }

    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {
                    if (adapter instanceof DragNDropAdapter) {
                        ((DragNDropAdapter)adapter).onDrop(from, to);
                        adapter.notifyDataSetChanged(-1);
                    }
                }
            };

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    if (adapter instanceof DragNDropAdapter) {
                        ((DragNDropAdapter)adapter).onRemove(which);
                        adapter.notifyDataSetChanged(-1);
                    }
                }
            };

    private DragListener mDragListener =
            new DragListener() {

                public void onDrag(int from, int to, View itemView) {
                    if (adapter instanceof DragNDropAdapter) {
                        ((DragNDropAdapter)adapter).onDrop(from, to);

                        adapter.notifyDataSetChanged(to);

                    }
                }

                public void onStartDrag(View itemView)
                {
                    //itemView.setBackgroundResource(R.drawable.selectable_drawable);
                }

                public void onStopDrag(View itemView) {
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setPressed(false);
                }

            };
}
