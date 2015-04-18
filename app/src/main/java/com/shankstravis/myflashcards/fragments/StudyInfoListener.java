package com.shankstravis.myflashcards.fragments;

/**
 * Created by Travis on 3/23/2015.
 */
// Interface implemented by FrontCardFragment and used when dialog box is displayed.
// It determines whether to continue from the previous study position or to restart
public interface StudyInfoListener {

    // If user wants to continue from previous study, the study position and grade position will
    // be sent to FrontCardFragment to continue from previous question
    void onContinueClick(int studyPosition, int gradePosition);

    // Will simply start from the first question in the deck
    void onRestartClick();
}
