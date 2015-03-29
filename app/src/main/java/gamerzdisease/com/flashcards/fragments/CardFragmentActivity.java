package gamerzdisease.com.flashcards.fragments;

import android.view.View;

import java.util.HashMap;

/**
 * Created by Travis on 3/18/2015.
 */
public interface CardFragmentActivity {
    void flipCardBack();
    void correctAnswer();
    void incorrectAnswer();
    void complete();
}

