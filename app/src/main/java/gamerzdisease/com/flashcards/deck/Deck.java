package gamerzdisease.com.flashcards.deck;

/**
 * Created by Travis on 1/31/2015.
 */
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Deck implements Parcelable, Serializable {

    private final static String TAG = "DECK";

    private String name;
    private LinkedHashMap<String, String> cards;

    public Deck(){
        this.cards = new LinkedHashMap();
    }

    public Deck(Deck deck){
        this.name = deck.name;
        this.cards = new LinkedHashMap(deck.cards);
    }

    public Deck(Parcel p){
        this.name = p.readString();
        this.cards = (LinkedHashMap)p.readHashMap(this.cards.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeMap(this.cards);
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel source) {
            return new Deck(source);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public HashMap<String, String> getCards(){
        return this.cards;
    }

    public void setCard(String key, String value){
        this.cards.put(key, value);
    }

}
