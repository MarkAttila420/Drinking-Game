package com.example.ivosjatek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private TextView test;
    private EditText darab;
    private Button crazy;
    private String[] strength = { "crazy", "strong", "normal", "weak", "pussy"};
    private String[] rand = {"spin the bottle", "wheeldecide"};
    private ArrayList<Task> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        crazy=findViewById(R.id.bCrazy);
        InputStream file = null;
        {
            try {
                file = getAssets().open("ivosjatekuj.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader reader;
        lista=new ArrayList<Task>();
        try{
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] bontottLine=line.split(";");
                int tempId=Integer.parseInt(bontottLine[0]);
                String tempText=bontottLine[1].toLowerCase(Locale.ROOT);
                String tempBait=bontottLine[2].toLowerCase(Locale.ROOT);
                int tempCategory=Integer.parseInt(bontottLine[3]);
                String[] tempkorty={bontottLine[4],bontottLine[5],bontottLine[6],bontottLine[7],bontottLine[8]};

                Task tempTask=new Task(tempId,tempText,tempBait,tempCategory,tempkorty);
                lista.add(tempTask);
                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item2,strength);
        aa.setDropDownViewResource(R.layout.spinner_item2);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        crazy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNames(spin.getSelectedItem().toString());
            }
        });


    }
    public void openNames(String i){
        Intent intent=new Intent(this,Names.class);
        intent.putExtra("difficulty", i);
        intent.putParcelableArrayListExtra("tasks",lista);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}