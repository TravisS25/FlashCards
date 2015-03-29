package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gamerzdisease.com.flashcards.adapters.DeckAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.DeckPositionReader;
import gamerzdisease.com.flashcards.filesystem.DeckReader;
import gamerzdisease.com.flashcards.filesystem.GradeReader;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DeckHolder mDeckInfo;
    private IStorageReader mDeckReader, mGradeReader;
    private HashMap<String, ArrayList<Double>> mGradeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initiateObjects();
        initiateStorage();
        readFromStorage();
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
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause Called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    // Activates when new deck button is pressed
    public void newDeck(View V) {
        initiateNewDeckIntent();
    }

    //==============================================================================================

    //Initializes all necessary objects
    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        Consts.DECK_FILEPATH = getFilesDir() + "/" + Consts.DECK_FILENAME;
        Consts.GRADE_FILEPATH = getFilesDir() + "/" + Consts.GRADE_FILENAME;
        mDeckReader = new DeckReader(Consts.DECK_FILEPATH);
        mGradeReader = new GradeReader(Consts.GRADE_FILEPATH);
    }

    //Initializes onclicklistener for listview
    private void initiateListener(){
        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), OptionsActivity.class);
                mDeckInfo.setDeckPosition(position);
                startActivity(intent);
            }
        };
    }

    //Starts activity for NewDeckActivity
    private void initiateNewDeckIntent() {
        Intent intent = new Intent(this, NewDeckActivity.class);
        startActivity(intent);
    }

    //Sets up and creates the listview of decks
    private void initiateListAdapter(){
        ListView listView = (ListView) findViewById(R.id.deck_listview);
        DeckAdapter deckAdapter = new DeckAdapter(mDeckInfo.getDeckList(), mGradeList);
        listView.setAdapter(deckAdapter);
        initiateListener();
        listView.setOnItemClickListener(mOnItemClickListener);
    }

    private void initiateStorage() {
        IStorageReader gradeStorage, deckPositionStorage;

        Consts.GRADE_FILEPATH = getFilesDir() + "/" + Consts.GRADE_FILENAME;
        Consts.DECK_POSITION_FILEPATH = getFilesDir() + "/" + Consts.DECK_POSITION_FILENAME;
        gradeStorage = new GradeReader(Consts.GRADE_FILEPATH);
        deckPositionStorage = new DeckPositionReader(Consts.DECK_POSITION_FILEPATH);

        try {
            mDeckReader.initiateStorage();
            gradeStorage.initiateStorage();
            deckPositionStorage.initiateStorage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readFromStorage(){
        ArrayList<Deck> deckList;
        ExecutorService service = Executors.newFixedThreadPool(6);
        Future<Object> deckFuture = service.submit(mDeckReader);
        Future<Object> gradeFuture = service.submit(mGradeReader);

        while(true) {
            try {
                if (deckFuture.isDone() && gradeFuture.isDone()) {
                    Log.d(TAG, "Future done");
                    deckList = new ArrayList<>((ArrayList<Deck>)deckFuture.get());
                    mGradeList = new HashMap<>((HashMap<String, ArrayList<Double>>)gradeFuture.get());
                    mDeckInfo.setDeckList(deckList);
                    return;
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }

}
