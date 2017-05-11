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
package com.sjalexander.dragdroplist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sjalexander.dragdroplist.ui.ListItem;
import com.sjalexander.dragdroplist.ui.adapters.DragDropAdapter;
import com.sjalexander.dragdroplist.ui.views.DragDropListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DragDropListView listView;
    private DragDropAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (DragDropListView) findViewById(R.id.list_view);
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
        listView.setAdapter(adapter);

    }
}
