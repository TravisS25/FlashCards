package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import gamerzdisease.com.flashcards.adapters.DeckAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.FileReader;
import gamerzdisease.com.flashcards.filesystem.FileWriter;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DeckHolder mDeckInfo;
    public static boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initiateObjects();
        if(flag) {
            initiateStorage();
            Log.d(TAG, "Flag reached");
        }
        initiateListAdapter();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Sets up and creates the listview of decks
    private void initiateListAdapter(){
        ListView listView = (ListView) findViewById(R.id.deck_listview);
        DeckAdapter deckAdapter = new DeckAdapter(mDeckInfo.getDeckList());
        listView.setAdapter(deckAdapter);
        initiateListener();
        listView.setOnItemClickListener(mOnItemClickListener);
    }

    //Creates files for decks and grades if they haven't already and retrieves decks from file
    //Copies decks to application variable mDeckInfo
    private void initiateStorage(){
        IStorageReader storage;
        ArrayList<Deck> deckList;

        Consts.DECK_FILEPATH = getFilesDir() + "/" + Consts.DECK_FILENAME;
        Consts.GRADE_FILEPATH = getFilesDir() + "/" + Consts.GRADE_FILENAME;
        storage = new FileReader(Consts.DECK_FILEPATH, Consts.GRADE_FILEPATH);

        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<ArrayList<Deck>> future = service.submit(storage);
        while(true) {
            try {
                if (future.isDone()) {
                    Log.d(TAG, "Future done");
                    deckList = new ArrayList<>(future.get());
                    mDeckInfo.setDeckList(deckList);
                    return;
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Initializes all necessary objects
    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

}
