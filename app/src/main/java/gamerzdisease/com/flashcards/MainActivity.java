package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gamerzdisease.com.flashcards.adapters.DeckAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;
import gamerzdisease.com.flashcards.filesystem.DeckPositionReader;
import gamerzdisease.com.flashcards.filesystem.DeckReader;
import gamerzdisease.com.flashcards.filesystem.GradeReader;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;
import gamerzdisease.com.flashcards.filesystem.StudyReader;
import gamerzdisease.com.flashcards.filesystem.StudyWriter;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private ArrayList<String> mDecks;
    private ArrayList<Double> mGrades;
    private DeckHolder mDeckInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initiateObjects();
        readDeckFromFile();
        //readGradesFromDatabase();
        initiateListAdapter();
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                Log.d(TAG, "ldpi");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.d(TAG, "mdpi");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.d(TAG, "hdpi");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.d(TAG, "xhdpi");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.d(TAG, "xxhdpi");
                break;
        }
        Log.d(TAG, "onCreate called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    // Activates when new deck button is pressed
    public void newDeck(View V) {
        initiateNewDeckIntent();
    }

    //==============================================================================================

    //Initializes all necessary objects
    private void initiateObjects() {
        mDeckInfo = (DeckHolder) getApplication();
        mDecks = new ArrayList<>();
        mGrades = new ArrayList<>();
    }

    //Starts activity for NewDeckActivity
    private void initiateNewDeckIntent() {
        Intent intent = new Intent(this, NewDeckActivity.class);
        startActivity(intent);
    }

    //Sets up and creates the listview of decks
    private void initiateListAdapter() {
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), OptionsActivity.class);
                mDeckInfo.setDeckPosition(position);
                startActivity(intent);
            }
        };

        ListView listView = (ListView) findViewById(R.id.deck_listview);
        DeckAdapter deckAdapter = new DeckAdapter(this, mDeckInfo.getDeckList(), mDecks, mGrades);
        listView.setAdapter(deckAdapter);
        listView.setOnItemClickListener(onItemClickListener);
    }

    //Reads deck ArrayList object from file and set the contents to the mDeckInfo variable
    private void readDeckFromFile() {
        Consts.DECK_FILEPATH = getFilesDir() + "/" + Consts.DECK_FILENAME;
        IStorageReader storageReader = new DeckReader(Consts.DECK_FILEPATH);
        try {
            storageReader.initiateStorage();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        ArrayList<Deck> deckList;
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Object> deckFuture = service.submit(storageReader);

        while (true) {
            try {
                if (deckFuture.isDone()) {
                    Log.d(TAG, "Future done");
                    deckList = new ArrayList<>((ArrayList<Deck>) deckFuture.get());
                    Log.d(TAG, "decklist value: " + deckList);
                    mDeckInfo.setDeckList(deckList);
                    return;
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Gets the max grade for each deck and assigns the decks to mDecks and grades to mGrades
    private void readGradesFromDatabase() {
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(this);
        String gradeTable = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;
        String deckColumn = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String gradeColumn = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;

        String query = "SELECT " + deckColumn + ", " + gradeColumn +
                       " FROM " + gradeTable +
                       " INNER JOIN " +
                           "(SELECT " + deckColumn + ", MAX(" + gradeColumn + ") AS " + gradeColumn +
                           " FROM " + gradeTable +
                           " GROUP BY " + deckColumn + ") USING (" + deckColumn + ", " + gradeColumn + ")";

        Cursor cursor = deckDatabaseAdapter.tableRawQuery(query, null);
        if(cursor.moveToNext()){
            while (!cursor.isAfterLast()){
                String deckName = cursor.getString(cursor.getColumnIndex(deckColumn));
                double grade = cursor.getDouble(cursor.getColumnIndex(gradeColumn));
                mDecks.add(deckName);
                mGrades.add(grade);
            }
        }
    }

}
