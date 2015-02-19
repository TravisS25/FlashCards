package gamerzdisease.com.flashcards;
/**
 * Created by Travis on 1/31/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;

public class NewDeckActivity extends Activity {

    private final static String TAG = "NewDeckActivity";
    private DeckHolder deckInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.new_deck_activity);
        deckInfo = (DeckHolder)getApplication();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createDeck(View V){
        if(deckNameExists(getDeckName()))
            Toast.makeText(this, Consts.DECK_NAME_EXISTS, Toast.LENGTH_SHORT).show();
        else if(!doesDeckHaveCharacters(getDeckName()))
            Toast.makeText(this, Consts.DECK_CHARACTERS, Toast.LENGTH_SHORT).show();
        else
            initiateNewCardIntent(getDeckName());

    }

    //===============================================================================================

    private String getDeckName(){
        EditText deckEditText = (EditText) findViewById(R.id.new_deck_text);
        return deckEditText.getText().toString();
    }

    private boolean deckNameExists(String userDeckName){
        for(Deck deck : this.deckInfo.getDeckList()){
            String fileDeckName = deck.getName();
            if (userDeckName.matches(fileDeckName))
                return true;
        }
        return false;
    }

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

    private void initiateNewCardIntent(String deckName){
        Intent intent = new Intent(this, CreateCardActivity.class);
        this.deckInfo.saveDeckName(deckName);
        intent.putExtra(Consts.DECKNAME, deckName);
        startActivity(intent);
    }

}