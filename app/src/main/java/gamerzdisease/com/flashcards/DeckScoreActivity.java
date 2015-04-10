package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.Grade;
import gamerzdisease.com.flashcards.deck.StudyMode;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;
import gamerzdisease.com.flashcards.filesystem.DeckPositionWriter;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;


/**
 * Created by Travis on 3/20/2015.
 */

//Activity is used to calculate and display the grade after user is done with study mode of a deck
public class DeckScoreActivity extends Activity {

    private final static String TAG = "DeckScoreActivity";
    private Deck mDeck;
    private DeckDatabaseAdapter mDeckDatabaseAdapter;
    private HashMap<String, Integer> mDeckPosition;
    private HashMap<String, ArrayList<Double>> mGradeList;
    private double mGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_score_activity);
        initiateObjects();
        displayGrade();
        writeGradeToStorage();
        removeDeckFromList();
        reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void toOptionsActivity(View v){
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


//================================================================================================

    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder)getApplication();
        ArrayList<Deck> deckList = deckInfo.getDeckList();
        int deckPostion = deckInfo.getDeckPosition();
        mDeck = deckList.get(deckPostion);
        mDeckDatabaseAdapter = new DeckDatabaseAdapter(this);
    }

    private void displayGrade(){
        TextView gradePercentage = (TextView)findViewById(R.id.deck_percentage);
        TextView gradeSize = (TextView)findViewById(R.id.deck_size);
        int deckSize = mDeck.getQuestions().size();

        mGrade = Grade.calculateGrade(deckSize);
        String percentage = String.format("%.1f", mGrade) + "%";
        String gradeCount = String.valueOf(Grade.getNumCorrect()) + "/" + String.valueOf(deckSize);

        gradePercentage.setText(percentage);
        gradeSize.setText(gradeCount);
    }

    private void writeGradeToStorage(){
        ContentValues contentValues = new ContentValues();
        String tableName = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;
        String deckColumn = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String gradeColumn = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;
        contentValues.put(deckColumn, mDeck.getName());
        contentValues.put(gradeColumn, mGrade);
        mDeckDatabaseAdapter.tableInsert(tableName, null, contentValues);

    }

    public void removeDeckFromList(){
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String whereClause = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN + " = ?";
        String[] whereArgs = {mDeck.getName()};
        mDeckDatabaseAdapter.tableRemove(tableName,whereClause, whereArgs);
    }

    private void reset(){
        StudyMode.resetPosition();
        Grade.resetCorrect();
    }


}
