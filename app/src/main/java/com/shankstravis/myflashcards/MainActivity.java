package com.shankstravis.myflashcards;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.shankstravis.myflashcards.adapters.DeckAdapter;
import com.shankstravis.myflashcards.deck.Consts;
import com.shankstravis.myflashcards.deck.Deck;
import com.shankstravis.myflashcards.deck.DeckHolder;
import com.shankstravis.myflashcards.filesystem.DeckReader;
import com.shankstravis.myflashcards.filesystem.IStorageReader;



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
}
