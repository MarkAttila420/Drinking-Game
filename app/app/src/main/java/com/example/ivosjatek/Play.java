package com.example.ivosjatek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Play extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView test;
    private Button bExit;
    private Button bNext;
    private Button bBack;
    private Button bStart;
    private Button bWheelDecide;
    private TextView pName;
    private Spinner spin;

    private AnimationDrawable ad2;
    private MediaPlayer mediaPlayer;


    private int id=0;
    private int millisecs=0;
    private int timerLength=15;
    private boolean timerStarted=false;
    private int currentPlayer=0;
    private int[] votes=new int[0];
    private ArrayList<Task> lista;
    private String strength;
    private ArrayList<Player> players;
    private String[] options = new String[0];
    private int baitKinyit=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        getSupportActionBar().hide();

        Bundle data = getIntent().getExtras();
        lista= data.getParcelableArrayList("tasks");
        strength =data.getString("difficulty");
        players=data.getParcelableArrayList("players");
        votes=new int[players.size()];
        int i=1;
        for(Player p: players){
            if(p.getName().equals("name")){
                p.setName("player "+i);
            }
            i++;
        }

        test=findViewById(R.id.test);
        bExit=findViewById(R.id.bExitPlay);
        bNext=findViewById(R.id.bNextPlay);
        bBack=findViewById(R.id.bBackPlay);
        bStart=findViewById(R.id.bStart);
        bWheelDecide=findViewById(R.id.bWheelDecide);
        pName=findViewById(R.id.tvPlayerName);
        spin = (Spinner) findViewById(R.id.spinner2);
        pName.setVisibility(View.GONE);

        Collections.shuffle(lista);

        setBackgrounds();
        next();

        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
        bWheelDecide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWheelDecide();
            }
        });
    }




    public void exit(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void next(){
        if(konnyu()){
            id++;
        }
        if(lista.size()<=id){
            pName.setVisibility(View.GONE);
            spin.setVisibility(View.GONE);
            bWheelDecide.setVisibility(View.GONE);
            bBack.setVisibility(View.GONE);
            bNext.setVisibility(View.GONE);
            bStart.setVisibility(View.GONE);
            test.setText("a játék véget ért!");
        }
        else {
            switch (lista.get(id).getCategory()) {
                case 1://ez a bait
                    pName.setVisibility(View.GONE);
                    spin.setVisibility(View.GONE);
                    bWheelDecide.setVisibility(View.GONE);
                    bStart.setVisibility(View.GONE);
                    if (baitKinyit == 0) {
                        bBack.setEnabled(false);
                        test.setText(lista.get(id).kiir(strength, lista.get(id).getText()));
                        baitKinyit = 1;
                    } else if (baitKinyit == 1) {
                        bBack.setEnabled(true);
                        test.setText(lista.get(id).kiirTeljes(strength, lista.get(id).getText()));
                        id++;
                        baitKinyit = 0;
                    }
                    break;
                case 2://ez a kategorias szavazo
                    pName.setVisibility(View.VISIBLE);
                    spin.setVisibility(View.VISIBLE);
                    bWheelDecide.setVisibility(View.GONE);
                    bStart.setVisibility(View.GONE);
                    bBack.setEnabled(false);
                    if (currentPlayer > 0) {
                        votes[currentPlayer - 1] = spin.getSelectedItemPosition();
                    }
                    if (currentPlayer != players.size()) {
                        pName.setText(players.get(currentPlayer).getName());
                        options = lista.get(id).getOptions();
                        spin.setOnItemSelectedListener(this);
                        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, options);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(aa);
                        spin.setSelection(0);
                        test.setText(lista.get(id).kiirVote(strength, lista.get(id).getText()));
                        currentPlayer++;
                    } else {
                        spin.setVisibility(View.GONE);
                        bBack.setEnabled(true);
                        int[] votes2 = new int[options.length];
                        for (int i : votes2) {
                            i = 0;
                        }
                        for (int i = 0; i < players.size(); i++) {
                            votes2[votes[i]]++;
                        }

                        String asd = "";
                        pName.setText(asd);
                        if (votes2.length == 2) {
                            asd = valaszt2(0, 1, votes2);
                        } else if (votes2.length == 3) {
                            asd = valaszt3(votes2, 0, 1, 2);
                        } else if (votes2.length == 4) {
                            if (votes2[0] == 0) {
                                asd = valaszt3(votes2, 1, 2, 3);
                            } else if (votes2[1] == 0) {
                                asd = valaszt3(votes2, 0, 2, 3);
                            } else if (votes2[2] == 0) {
                                asd = valaszt3(votes2, 0, 1, 3);
                            } else if (votes2[3] == 0) {
                                asd = valaszt3(votes2, 0, 1, 2);
                            }//itt mindegyikre volt legalabb egy szavazat
                            else {
                                int minimum = 10;
                                ArrayList<Integer> minimumok = new ArrayList<>();
                                int asd2 = 0;
                                for (int i : votes2) {
                                    if (minimum > i) {
                                        minimum = i;
                                    }
                                }
                                for (int i = 0; i < votes2.length; i++) {
                                    if (minimum == votes2[i]) {
                                        asd2++;
                                        minimumok.add(i);
                                    }
                                }
                                switch (asd2) {
                                    case 1:
                                        asd = valaszt(minimumok.get(0), votes2);
                                        break;
                                    case 2:
                                        asd = valaszt(minimumok.get(0), minimumok.get(1), votes2);
                                        break;
                                    case 3:
                                        asd = valaszt(minimumok.get(0), minimumok.get(1), minimumok.get(2), votes2);
                                        break;
                                }
                            }
                        }

                        pName.setText(asd);
                        test.setText(lista.get(id).kiirVoteTeljes(strength, lista.get(id).getText()));
                        currentPlayer = 0;
                        id++;
                    }
                    break;
                case 3://ez a player szavazó
                    pName.setVisibility(View.VISIBLE);
                    spin.setVisibility(View.VISIBLE);
                    bWheelDecide.setVisibility(View.GONE);
                    bStart.setVisibility(View.GONE);
                    bBack.setEnabled(false);
                    if (currentPlayer > 0) {
                        votes[currentPlayer - 1] = spin.getSelectedItemPosition();
                    }
                    if (currentPlayer != players.size()) {
                        pName.setText(players.get(currentPlayer).getName());
                        options = new String[players.size()];
                        for (int i = 0; i < options.length; i++) {
                            options[i] = players.get(i).getName();
                        }
                        spin.setOnItemSelectedListener(this);
                        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, options);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(aa);
                        spin.setSelection(0);
                        test.setText(lista.get(id).kiirVote(strength, lista.get(id).getText()));
                        currentPlayer++;
                    } else if (currentPlayer >= players.size()) {
                        spin.setVisibility(View.GONE);
                        bBack.setEnabled(true);

                        String asd = "";
                        int[] votes2 = new int[options.length];
                        for (int i : votes2) {
                            i = 0;
                        }
                        for (int i = 0; i < players.size(); i++) {
                            votes2[votes[i]]++;
                        }
                        int max = 0;
                        int maxdb = 0;
                        for (int i = 0; i < votes2.length; i++) {
                            if (votes2[i] > max) {
                                max = votes2[i];
                            }
                        }
                        for (int i = 0; i < votes2.length; i++) {
                            if (votes2[i] == max) {
                                maxdb++;
                            }
                        }
                        int maxdb2 = maxdb;
                        for (int i = 0; i < votes2.length; i++) {
                            if (votes2[i] == max) {
                                asd += players.get(i).getName();
                                maxdb--;
                                if (maxdb > 0) {
                                    asd += ", ";
                                }
                            }
                        }
                        asd += " " + lista.get(id).kiir(strength,lista.get(id).getBait());
                        pName.setText(asd);
                        currentPlayer = 0;
                        id++;
                    }
                    break;
                case 4:
                    pName.setVisibility(View.GONE);
                    spin.setVisibility(View.GONE);
                    bWheelDecide.setVisibility(View.VISIBLE);
                    bStart.setVisibility(View.GONE);
                    bBack.setEnabled(true);

                    test.setText(lista.get(id).kiirTeljes(strength, lista.get(id).getText()));
                    id++;
                    break;
                case 5:
                    pName.setVisibility(View.GONE);
                    spin.setVisibility(View.GONE);
                    bWheelDecide.setVisibility(View.VISIBLE);
                    bStart.setVisibility(View.GONE);

                    if (baitKinyit == 0) {
                        test.setText(lista.get(id).kiirBaitSpin(strength, lista.get(id).getText()));
                        bBack.setEnabled(false);
                        baitKinyit = 1;
                    } else if (baitKinyit == 1) {
                        test.setText(lista.get(id).kiirTeljes(strength, lista.get(id).getText()));
                        bBack.setEnabled(true);
                        id++;
                        baitKinyit = 0;
                    }
                    break;
                case 6://ez a timeres
                    pName.setVisibility(View.VISIBLE);
                    spin.setVisibility(View.GONE);
                    bWheelDecide.setVisibility(View.VISIBLE);
                    bStart.setVisibility(View.VISIBLE);
                    bStart.setEnabled(true);
                    test.setText(lista.get(id).kiir(strength, lista.get(id).getText()));
                    bBack.setEnabled(true);

                    millisecs=getTime();

                    int minutes=millisecs/60000;
                    int secs=(millisecs%60000)/1000;
                    int millis=millisecs%1000;

                    String time=String.format(Locale.getDefault(),"%02d:%02d:%03d",minutes,secs,millis);
                    pName.setText(time);
                    id++;
                    break;
                default://EZ JO
                    bBack.setEnabled(true);
                    pName.setVisibility(View.GONE);
                    spin.setVisibility(View.GONE);
                    bWheelDecide.setVisibility(View.GONE);
                    bStart.setVisibility(View.GONE);
                    test.setText(lista.get(id).kiirTeljes(strength, lista.get(id).getText()));
                    id++;
                    break;

            }
        }
    }
    private void StartTicking(){
        if(mediaPlayer==null){
            mediaPlayer= MediaPlayer.create(this,R.raw.loop);
        }else{
            mediaPlayer=null;
            mediaPlayer= MediaPlayer.create(this,R.raw.loop);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        bBack.setEnabled(false);
        bNext.setEnabled(false);
    }
    private void StopTicking(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        mediaPlayer=MediaPlayer.create(this,R.raw.ring);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        bBack.setEnabled(true);
        bNext.setEnabled(true);
    }
    private int getTime(){
        switch (strength){//"crazy", "strong", "normal", "weak", "pussy"
            case "crazy":
                return lista.get(id).getTime(0)*1000;
            case "strong":
                return lista.get(id).getTime(1)*1000;
            case "normal":
                return lista.get(id).getTime(2)*1000;
            case "weak":
                return lista.get(id).getTime(3)*1000;
            default:
                return lista.get(id).getTime(4)*1000;
        }
    }
    public void startTimer() {
        id--;
        timerStarted=true;
        bStart.setEnabled(false);
        millisecs=getTime();
        StartTicking();

        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes=millisecs/60000;
                int secs=(millisecs%60000)/1000;
                int millis=millisecs%1000;

                String time=String.format(Locale.getDefault(),"%02d:%02d:%03d",minutes,secs,millis);
                pName.setText(time);
                if(millisecs<=0){
                    timerStarted=false;
                    bStart.setEnabled(true);
                    test.setText(lista.get(id).kiirTeljes(strength, lista.get(id).getText()));
                    StopTicking();
                    id++;
                }else{
                    millisecs-=10;
                    handler.postDelayed(this,10);
                }
            }
        });

    }
    public void back(){
        if(id>1){
            id-=2;
            if(konnyu()){
                id--;
            }
            baitKinyit=0;
            currentPlayer=0;
            options = new String[0];
            next();
        }
    }
    public void openWheelDecide(){



        ad2.start();
        Random rand=new Random();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ad2.stop();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Play.this);

                View mView =getLayoutInflater().inflate(R.layout.player_chosen_dialog,null);
                Button mOk = (Button)mView.findViewById(R.id.bPlayerChosenOk);
                TextView mText = (TextView)mView.findViewById(R.id.player_chosen_text);
                mText.setText(players.get(rand.nextInt(players.size())).getName());

                mBuilder.setView(mView);
                AlertDialog ad=mBuilder.create();
                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                ad.show();
            }
        }, rand.nextInt(2000)+1000);

    }
    public String valaszt(int elso,int masodik,int harmadik,int[] votes2){
        int asd2=0;
        String asd="";
        for(int i=0;i<players.size();i++){
            if(votes[i]==elso||votes[i]==masodik||votes[i]==harmadik){
                asd+=players.get(i).getName();
                asd2++;
                if(asd2<(votes2[elso]+votes2[masodik]+votes2[harmadik])){
                    asd+=", ";
                }
            }
        }
        asd+=" iszik";
        return asd;
    }
    public String valaszt(int elso,int masodik,int[] votes2){
        int asd2=0;
        String asd="";
        for(int i=0;i<players.size();i++){
            if(votes[i]==elso||votes[i]==masodik){
                asd+=players.get(i).getName();
                asd2++;
                if(asd2<(votes2[elso]+votes2[masodik])){
                    asd+=", ";
                }
            }
        }
        asd+=" iszik";
        return asd;
    }
    public String valaszt(int elso,int[] votes2){
        int asd2=0;
        String asd="";
        for(int i=0;i<players.size();i++){
            if(votes[i]==elso){
                asd+=players.get(i).getName();
                asd2++;
                if(asd2<votes2[elso]){
                    asd+=", ";
                }
            }
        }
        asd+=" iszik";
        return asd;
    }
    public String valaszt2(int elso, int masodik,int[] votes2){
        String asd="";
        int asd2=0;
        if(votes2[elso]==0||votes2[masodik]==0){
            asd="mindenki iszik";
        }
        else if(votes2[elso]==votes2[masodik]){
            asd="mindenki iszik!";
        }
        else if(votes2[elso]<votes2[masodik]){
            asd=valaszt(elso,votes2);
        }
        else{
            asd=valaszt(masodik,votes2);
        }
        return asd;
    }
    public String valaszt3(int[] votes2,int egy, int ketto, int harom){
        String asd="";
        if(votes2[egy]==votes2[ketto]&&votes2[egy]==votes2[harom]){
            asd="mindenki iszik";
        }
        else if(votes2[egy]==players.size()||votes2[ketto]==players.size()||votes2[harom]==players.size()){
            asd="mindenki iszik";
        }
        else if(votes2[egy]==0){//0-ra nem szavazott senki
            asd=valaszt2(ketto,harom,votes2);
        }
        else if(votes2[ketto]==0){//1-re nem szavazott senki
            asd=valaszt2(egy,harom,votes2);
        }
        else if(votes2[harom]==0){//2-re nem szavazott senki
            asd=valaszt2(ketto,egy,votes2);
        }
        else if(votes2[egy]>votes2[ketto]&&votes2[ketto]==votes2[harom]){//1-re és 2-re ugyanannyian, de kevesebben szavaztak, mint 0-ra
            asd=valaszt(ketto,harom,votes2);
        }
        else if(votes2[ketto]>votes2[egy]&&votes2[egy]==votes2[harom]){//0-ra és 2-re ugyanannyian, de kevesebben szavaztak, mint 1-re
            asd=valaszt(egy,harom,votes2);
        }
        else if(votes2[harom]>votes2[ketto]&&votes2[ketto]==votes2[egy]){//1-re és 0-ra ugyanannyian, de kevesebben szavaztak, mint 2-re
            asd=valaszt(egy,ketto,votes2);
        }
        else if(votes2[egy]<votes2[ketto]&&votes2[egy]<votes2[harom]){//0-ra szavaztak a legkevesebben
            asd=valaszt(egy,votes2);
        }
        else if(votes2[ketto]<votes2[harom]&&votes2[ketto]<votes2[egy]){//1-re szavaztak a legkevesebben
            asd=valaszt(ketto,votes2);
        }
        else if(votes2[harom]<votes2[ketto]&&votes2[harom]<votes2[egy]){//2-re szavaztak a legkevesebben
            asd=valaszt(harom,votes2);
        }
        return asd;

    }

    public boolean konnyu(){
        if(strength.equals("normal")||strength.equals("weak")||strength.equals("pussy")){
            if(lista.get(id).getKorty()==-1){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void setBackgrounds(){

        ImageView b_polc=findViewById(R.id.play_bpolc);
        AnimationDrawable adb_polc=(AnimationDrawable) b_polc.getBackground();
        adb_polc.setEnterFadeDuration(60000);
        adb_polc.setExitFadeDuration(3000);
        adb_polc.start();

        ImageView j_polc=findViewById(R.id.play_jpolc);
        AnimationDrawable adj_polc=(AnimationDrawable) j_polc.getBackground();
        adj_polc.setEnterFadeDuration(60000);
        adj_polc.setExitFadeDuration(3000);
        adj_polc.start();

        ImageView tabla=findViewById(R.id.play_tabla);
        AnimationDrawable adtabla=(AnimationDrawable) tabla.getBackground();
        adtabla.setEnterFadeDuration(60000);
        adtabla.setExitFadeDuration(3000);
        adtabla.start();

        ImageView pult=findViewById(R.id.play_pult);
        AnimationDrawable adpult=(AnimationDrawable) pult.getBackground();
        adpult.setEnterFadeDuration(60000);
        adpult.setExitFadeDuration(3000);
        adpult.start();

        ad2=(AnimationDrawable) bWheelDecide.getBackground();
        ad2.setEnterFadeDuration(0);
        ad2.setExitFadeDuration(0);

    }
}