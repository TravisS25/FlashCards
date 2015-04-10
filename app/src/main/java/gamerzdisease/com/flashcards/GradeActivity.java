package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gamerzdisease.com.flashcards.adapters.GradeAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;
import gamerzdisease.com.flashcards.filesystem.GradeReader;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;

/**
 * Created by Travis on 2/13/2015.
 */
public class GradeActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private HashMap<String, ArrayList<Double>> mGradeList;
    private Cursor mCursor;
    private String mDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        readGradeFromStorage();
        if(isGradeForDeck()) {
            setContentView(R.layout.grades_activity);
            initiateListAdapter();
        }
        else
            setContentView(R.layout.no_grade);
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

//=================================================================================================

    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder)getApplication();
        Deck deck = deckInfo.getDeckList().get(deckInfo.getDeckPosition());
        mDeckName = deck.getName();
    }

    private boolean isGradeForDeck(){
        return mCursor.getCount() != 0;
    }

    private void readGradeFromStorage(){
        String tableName = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;
        String id = DeckDatabaseAdapter.DeckHelper.UID_COLUMN;
        String deckColumn = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String gradeColumn = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;
        String[] columns = {id, gradeColumn};
        String selection = deckColumn + " = ?";
        String[] selectionArgs = {mDeckName};
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(this);
        mCursor = deckDatabaseAdapter.tableQuery(tableName, columns, selection, selectionArgs, null, null, null, null);
    }

    private void initiateListAdapter(){
        ListView listView = (ListView)findViewById(R.id.grade_listview);
        GradeAdapter gradeAdapter = new GradeAdapter(this, mCursor);
        listView.setAdapter(gradeAdapter);
    }
}


