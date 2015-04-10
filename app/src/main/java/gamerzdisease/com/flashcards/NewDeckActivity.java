package gamerzdisease.com.flashcards;
/**
 * Created by Travis on 1/31/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InterruptedIOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.DeckWriter;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;

public class NewDeckActivity extends Activity {

    private final static String TAG = "NewDeckActivity";
    private DeckHolder mDeckInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.new_deck_activity);
        initiateObjects();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Activates when user clicks "Create Deck" button
    //Contains checks for deck name
    public void createDeck(View V){
        if(deckNameExists(getDeckName()))
            Toast.makeText(this, Consts.DECK_NAME_EXISTS, Toast.LENGTH_SHORT).show();
        else if(!doesDeckHaveCharacters(getDeckName()))
            Toast.makeText(this, Consts.DECK_CHARACTERS, Toast.LENGTH_SHORT).show();
        else if(deckCharacterLimit())
            Toast.makeText(this, Consts.DECK_CHARACTER_LIMIT, Toast.LENGTH_SHORT).show();
        else {
            storeDeckName(getDeckName());
            initiateMainActivityIntent();
        }

    }


    //===============================================================================================

    //Initializes class variables
    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    //Sets deck name to class variable mDeckEditText
    private String getDeckName(){
        EditText deckEditText = (EditText) findViewById(R.id.new_deck_text);
        return deckEditText.getText().toString();
    }

    //Checks if given deck name exists in application variable mDeckInfo
    private boolean deckNameExists(String userDeckName){
        for(Deck deck : mDeckInfo.getDeckList()){
            String fileDeckName = deck.getName();
            if (userDeckName.matches(fileDeckName))
                return true;
        }
        return false;
    }

    //Checks if deck name only contains alphabet characters or numbers
    private boolean doesDeckHaveCharacters(String deckName){
        String pattern = "^\\d*[a-zA-Z][a-zA-Z0-9]*$";
        Pattern r = Pattern.compile(pattern);
        deckName.trim();
        Matcher m = r.matcher(deckName);

        if(!m.find())
            return false;
        else
            return true;
    }

    //Checks if deck name does not exceed character limit
    private boolean deckCharacterLimit(){
        int characterLimit = 12;
        return getDeckName().length() > characterLimit;
    }

    //Starts intent for MainActivity and adds deck name to application variable mDeckInfo
    //Also writes to storage
    private void initiateMainActivityIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void storeDeckName(String deckName){
        IStorageWriter storageWriter;
        Thread t1;

        mDeckInfo.setDeck(deckName);
        mDeckInfo.getDeckList().add(mDeckInfo.getDeck());

        storageWriter = new DeckWriter(mDeckInfo.getDeckList(), Consts.DECK_FILEPATH);
        t1 = new Thread(storageWriter);
        t1.start();
        try {
            t1.join();
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
}