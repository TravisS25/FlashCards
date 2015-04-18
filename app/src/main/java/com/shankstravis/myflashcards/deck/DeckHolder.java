package com.shankstravis.myflashcards.deck;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Travis on 2/15/2015.
 */
public class DeckHolder extends Application {

    private final static String TAG = "Deckholder";
    private ArrayList<Deck> mDeckList;
    private Deck mDeck;
    private int mDeckPosition;
    private String mQuestion, mAnswer;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate called");
        initiateMainObjects();
    }

    public void setDeck(String deckName){
        mDeck = new Deck(deckName);
    }

    public Deck getDeck(){
        return mDeck;
    }

    public void setDeckPosition(int position){
        mDeckPosition = position;
    }

    public int getDeckPosition(){
        return mDeckPosition;
    }

    public void setDeckList(ArrayList<Deck> deckList){
        mDeckList = new ArrayList<>(deckList);
    }

    public ArrayList<Deck> getDeckList(){
        return mDeckList;
    }

    public void setQuestion(String question){mQuestion = question; }

    public String getQuestion(){ return mQuestion;}

    public void setAnswer(String answer){mAnswer = answer;}

    public String getAnswer(){ return mAnswer; }

//==================================================================================================

    private void initiateMainObjects(){
        mDeckList = new ArrayList<>();
    }

}
