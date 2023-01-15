package com.example.ivosjatek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;

public class Names<PlayerAdapter> extends AppCompatActivity {

    private ArrayList<Player> players;
    private Button add;
    private Button remove;
    private PlayersAdapter adapter;
    private RecyclerView rvPlayers;
    private Button next;
    private Button back;

    private ArrayList<Task> lista;
    private String strength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_names);
        getSupportActionBar().hide();
        rvPlayers = (RecyclerView) findViewById(R.id.rvPlayers);
        add=findViewById(R.id.bAdd);
        remove=findViewById(R.id.bRemove);
        players=new ArrayList<>();
        players=Player.addPlayer(players);
        players=Player.addPlayer(players);
        adapter = new PlayersAdapter(players);
        rvPlayers.setAdapter(adapter);
        next=findViewById(R.id.bNextNames);
        back=findViewById(R.id.bBackNames);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));

        Bundle data = getIntent().getExtras();
        lista= data.getParcelableArrayList("tasks");
        strength =data.getString("difficulty");



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayer();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePlayer();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }
    public void addPlayer(){
        players=Player.addPlayer(players);
        adapter = new PlayersAdapter(players);
        rvPlayers.setAdapter(adapter);
    }
    public void removePlayer(){
        players=Player.removePlayer(players);
        adapter = new PlayersAdapter(players);
        rvPlayers.setAdapter(adapter);
    }
    public void goNext(){
        Intent intent=new Intent(this,Play.class);
        intent.putExtra("difficulty", strength);
        intent.putParcelableArrayListExtra("tasks",lista);
        intent.putParcelableArrayListExtra("players", players);
        startActivity(intent);
    }
    public void goBack(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}