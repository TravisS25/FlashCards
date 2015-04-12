package gamerzdisease.com.flashcards.fragments;

import java.util.HashMap;

/**
 * Created by Travis on 3/23/2015.
 */
public interface DialogClickListener {
    public void onContinueClick(int studyPosition, int gradePosition);
    public void onRestartClick();
}
