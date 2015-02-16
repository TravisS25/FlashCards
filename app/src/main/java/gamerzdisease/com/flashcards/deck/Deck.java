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

public class Deck implements Parcelable {

    private final static String TAG = "DECK";

    private String name;
    private HashMap<String, String> card;

    public Deck(){}

    public Deck(Deck deck){
        this.name = deck.name;
        this.card = new HashMap<>(deck.card);
    }

    public Deck(Parcel p){
        this.name = p.readString();
        this.card = p.readHashMap(this.card.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeMap(this.card);
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

    public HashMap<String, String> getCard(){
        return this.card;
    }

    public void setCard(String key, String value){
        this.card.put(key, value);
    }

}
