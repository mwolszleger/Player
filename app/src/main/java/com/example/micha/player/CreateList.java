package com.example.micha.player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateList extends AppCompatActivity {
    private SongAdapter songAdt;
    private EditText name;
    private static ListView songView;
    private ArrayList<Song> newList=new ArrayList<Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        songView = (ListView) findViewById(R.id.create_list);
        name = (EditText) findViewById(R.id.listName);
        songAdt = new SongAdapter(this, PlayLists.allSongsList);
        songView.setAdapter(songAdt);
        songView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    public void songPicked(View view){

        int id = Integer.parseInt(view.getTag().toString());
       newList.add(PlayLists.allSongsList.get(id));
    }
    public void add(View view){

        if(name.getText().toString().length()==0||newList.size()==0)
            return;
        PlayLists.listsNames.add(name.getText().toString());
        PlayLists.lists.add(newList);
        Log.e("jeden",Integer.toString(newList.size()));
        finish();
    }

    }

