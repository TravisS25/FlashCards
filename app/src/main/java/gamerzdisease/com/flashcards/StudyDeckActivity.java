package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.Grade;
import gamerzdisease.com.flashcards.deck.StudyMode;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;
import gamerzdisease.com.flashcards.fragments.CardBackFragment;
import gamerzdisease.com.flashcards.fragments.CardFrontFragment;
import gamerzdisease.com.flashcards.fragments.CardFragmentActivity;
import gamerzdisease.com.flashcards.fragments.FragmentFileWriter;

/**
 * Created by Travis on 2/8/2015.
 */

public class StudyDeckActivity extends Activity implements FragmentManager.OnBackStackChangedListener, CardFragmentActivity {

    private final static String TAG = "StudyDeckActivity";
    private Handler mHandler;
    private FragmentFileWriter mFragmentFileWriter;
    private Deck mDeck;

    public StudyDeckActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_deck_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();

        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.

            CardFrontFragment cardFrontFragment = new CardFrontFragment();

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, cardFrontFragment)
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
                if(findViewById(R.id.no_card_layout) == null) {
                    storeStudyInfo();
                    reset();
                }
                else
                    reset();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "no card output: " + findViewById(R.id.no_card_layout));
        if(findViewById(R.id.no_card_layout) == null) {
            storeStudyInfo();
            super.onBackPressed();
        }
        else{
            Log.d(TAG, "Made to else");
            reset();
            super.onBackPressed();
        }
    }

    @Override
    public void flipCardBack(){
        flipCardToBack();
    }

    // Activates when user clicks "Correct" button.  Increases grade and study position and
    // flips the card back to front
    @Override
    public void incorrectAnswer(){
        if(mDeck.getQuestions().size() > StudyMode.getPosition()) {
            flipCardToFront();
        }
        else{
            Intent intent = new Intent(this, DeckScoreActivity.class);
            startActivity(intent);
        }
    }

    // Activates when user clicks "Correct" button.  Increases grade and study position and
    // flips the card back to front
    @Override
    public void correctAnswer(){
        if(mDeck.getQuestions().size() > StudyMode.getPosition()) {
            flipCardToFront();
        }
        else{
            Intent intent = new Intent(this, DeckScoreActivity.class);
            startActivity(intent);
        }
    }

    // Activates when user clicks "Done" button.  Stores study progress and goes back to options activity
    @Override
    public void complete(){
        storeStudyInfo();
        reset();
        initiateOptionsIntent();
    }



//==================================================================================================

    private void initiateObjects(){
        DeckHolder DeckInfo = (DeckHolder)getApplication();
        mHandler = new Handler();
        mDeck = new Deck(DeckInfo.getDeckList().get(DeckInfo.getDeckPosition()));
    }

    private void initiateOptionsIntent(){
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    private void reset(){
        StudyMode.resetPosition();
        Grade.resetCorrect();
    }

    // When user clicks back or navigates back without finishing the deck, their progress will
    // be stored to database
    private void storeStudyInfo(){
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(this);
        ContentValues contentValues = new ContentValues();
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String deckColumn = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String studyPositionColumn = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        String questionsAnsweredColumn = DeckDatabaseAdapter.DeckHelper.REMAINING_QUESTIONS_COLUMN;
        String gradePositionColumn = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;
        String studyModeColumn = DeckDatabaseAdapter.DeckHelper.STUDY_MODE_COLUMN;

        contentValues.put(deckColumn, mDeck.getName());
        contentValues.put(studyPositionColumn, StudyMode.getPosition());
        contentValues.putNull(questionsAnsweredColumn);
        contentValues.put(gradePositionColumn, Grade.getNumCorrect());
        contentValues.put(studyModeColumn, StudyMode.IN_ORDER_MODE);
        deckDatabaseAdapter.tableReplace(tableName, studyPositionColumn, contentValues);
    }
/*
    private void storeStudyPosition(){
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(this);
        ContentValues contentValues = new ContentValues();
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_TABLE;
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String studyPosition = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        int position = StudyMode.getPosition();
        contentValues.put(deckColumnName, mDeck.getName());
        contentValues.put(studyPosition, position);
        deckDatabaseAdapter.tableReplace(tableName, null, contentValues);
    }

    private void storeGrade(){
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(this);
        ContentValues contentValues = new ContentValues();
        String tableName = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_TABLE;
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String studyPosition = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;
        int position = Grade.getNumCorrect();
        contentValues.put(deckColumnName, mDeck.getName());
        contentValues.put(studyPosition, position);
        deckDatabaseAdapter.tableReplace(tableName, null, contentValues);
    }
*/
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

}
