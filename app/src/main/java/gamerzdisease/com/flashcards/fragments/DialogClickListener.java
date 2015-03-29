package gamerzdisease.com.flashcards.fragments;

import java.util.HashMap;

/**
 * Created by Travis on 3/23/2015.
 */
public interface DialogClickListener {
    public void onContinueClick(HashMap<String, Integer> deckPosition);
    public void onRestartClick();
}
