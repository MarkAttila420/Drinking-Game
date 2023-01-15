package com.example.ivosjatek;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Task implements Parcelable {
    private String text;
    private String bait;
    private int category;
    int id;
    private String[] korty =new String[5];

    public Task(int tempId,String tempText,String tempBait,int tempCategory,String[] tempKorty){
        this.id=tempId;
        this.text=tempText;
        this.bait=tempBait;
        this.category=tempCategory;
        for(int i=0;i<5;i++){
            korty[i]=new String(tempKorty[i]);
        }
    }
    public String getText() {
        return text;
    }
    public String getBait(){return bait;}
    public int getKorty(){
        return Integer.parseInt(korty[0].split("\\^")[0]);
    }
    public int getTime(int difficulty){return Integer.parseInt(korty[difficulty]);}
    public int getCategory(){return category;}

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Task(Parcel in) {
        id=in.readInt();
        text = in.readString();
        bait = in.readString();
        category = in.readInt();
        korty = in.createStringArray();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(text);
        parcel.writeString(bait);
        parcel.writeInt(category);
        parcel.writeStringArray(korty);
    }

    public String kiirVote(String strength,String szo){
        String[] ketResz=szo.split("\\^");
        return kiir(strength,ketResz[0]);
    }
    public String kiirVoteTeljes(String strength,String szo){
        String[] ketResz=szo.split("\\^");
        return kiir(strength,ketResz[0])+" "+kiir(strength,ketResz[1]);

    }
    public String kiirTeljes(String strength, String szo){
        szo=szo.replaceFirst("@",bait);
        szo=kiir(strength,szo);
        return szo;
    }
    public String[] getOptions(){
        String[] options=bait.split("\\^");

        return options;
    }
    public String kiirBaitSpin(String strength, String szo){
        szo= szo.replace("@","");
        return kiir(strength,szo);
    }

    public String kiir(String strength, String szo){
        String[] kortyok = new String[0];
        szo=szo.replace("@","...");
        switch(strength){
            case "crazy":
                kortyok=korty[0].split("\\^");
                break;
            case "strong":
                kortyok=korty[1].split("\\^");
                break;
            case "normal":
                kortyok=korty[2].split("\\^");
                break;
            case "weak":
                kortyok=korty[3].split("\\^");
                break;
            case "pussy":
                kortyok=korty[4].split("\\^");
                break;
        }
        if(!kortyok[0].contains("_")){
            if(Integer.valueOf(kortyok[0])<=0){
                return szo;
            }
        }
        for(int i=0;i< kortyok.length;i++){
            if(kortyok[i].contains("_")){
                szo=szo.replaceFirst("#","");
                if(szo.contains("/")){
                    szo=szo.replaceFirst("/","");
                }
            }
            else{
            szo=szo.replaceFirst("#",kortyok[i]);
            }
        }
        return szo;
    }

}
