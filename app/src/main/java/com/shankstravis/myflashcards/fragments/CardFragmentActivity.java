package com.shankstravis.myflashcards.fragments;

/**
 * Created by Travis on 3/18/2015.
 */
// Interface implemented by StudyDeckActivity.  Used mainly to animate between fragments when a
// button is clicked
public interface CardFragmentActivity {

    // Activates when user clicks the "answer" button to flip to the back card fragment
    void flipCardBack();

    // Activates when user clicks the "correct" button on the back card fragment
    // Increments the number of correct answers in the StudyMode class and flips card to front
    // card fragment
    void correctAnswer();

    // Activates when user clicks the "incorrect" button on the back card fragment
    // Flips card to fron card fragment
    void incorrectAnswer();

    // Activates when user clicks the "done" button on the back card fragment
    // Activates the function that saves the user progress and sends user to the option activity
    void complete();
}

