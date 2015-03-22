package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.Grade;
import gamerzdisease.com.flashcards.deck.StudyMode;
import gamerzdisease.com.flashcards.fragments.CardBackFragment;
import gamerzdisease.com.flashcards.fragments.CardFrontFragment;
import gamerzdisease.com.flashcards.fragments.IAnimation;

/**
 * Created by Travis on 2/8/2015.
 */

public class StudyDeckActivity extends Activity implements FragmentManager.OnBackStackChangedListener, IAnimation {

    private Handler mHandler;
    private DeckHolder mDeckInfo;
    private Deck mDeck;

    public StudyDeckActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_deck_activity);
        initiateObjects();
        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }



    public void flipCardBack(){
        flipCardToBack();
    }

    public void incorrectAnswer(){
        if(mDeck.getQuestions().size() > StudyMode.getPosition()) {
            flipCardToFront();
        }
        else{
            Intent intent = new Intent(this, DeckScoreActivity.class);
            startActivity(intent);
        }
    }

    public void correctAnswer(){
        if(mDeck.getQuestions().size() > StudyMode.getPosition()) {
            flipCardToFront();
        }
        else{
            Intent intent = new Intent(this, DeckScoreActivity.class);
            startActivity(intent);
        }
    }

    public void complete(){

    }


//==================================================================================================

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        mHandler = new Handler();
        mDeck = new Deck(mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition()));
    }

    private void flipCardToBack() {
        CardBackFragment cardBackFragment = new CardBackFragment();

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, cardBackFragment)

                        // Commit the transaction.
                .commit();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    private void flipCardToFront(){
        CardFrontFragment cardFrontFragment = new CardFrontFragment();

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, cardFrontFragment)

                        // Commit the transaction.
                .commit();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    private void deckComplete(){
        CardFrontFragment cardFrontFragment = new CardFrontFragment();


    }

}
