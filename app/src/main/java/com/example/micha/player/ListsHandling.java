package com.example.micha.player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListsHandling extends AppCompatActivity {
ListView listView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onStart() {
        super.onStart();
       adapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_handling);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PlayLists.listsNames);
        listView = (ListView)findViewById(R.id.listsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //view.setBackgroundColor(Color.TRANSPARENT);
                MainActivity.songList=PlayLists.lists.get(i);

                finish();

            }
        });
    }

    public void newList(View view)
    {
        Intent intent_newList = new Intent(this, CreateList.class);
        startActivity(intent_newList);

    }
}
