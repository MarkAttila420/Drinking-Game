package com.example.ivosjatek;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Player implements Parcelable {
    private String mName;

    public Player(String name) {
        mName = name;
    }

    protected Player(Parcel in) {
        mName = in.readString();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getName() {
        return mName;
    }
    public String getName2(){
        String asd=mName.replaceAll(" ","+");
        return asd;
    }
    public void setName(String uj){mName=uj;}

    public static ArrayList<Player> addPlayer(ArrayList<Player> old){
        ArrayList<Player> players=new ArrayList<>();
        players.addAll(old);
        players.add(new Player("name"));

        return players;
    }
    public static ArrayList<Player> removePlayer(ArrayList<Player> old){
        if(old.size()!=2) {
            ArrayList<Player> players = new ArrayList<>();
            players.addAll(old);
            players.remove(players.size() - 1);
            return players;
        }
        return old;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
    }
}
