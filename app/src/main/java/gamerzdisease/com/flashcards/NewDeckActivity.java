package gamerzdisease.com.flashcards;
/**
 * Created by Travis on 1/31/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewDeckActivity extends Activity {

    public final static String DECKNAME = "gamerzdisease.com.flashcards.DECKNAME";
    private final static String FILENAME = "decks";
    private final static String BLANK_DECK_NAME = "You must enter a deck name";
    private final static String DECK_NAME_EXISTS = "That deck name already exists";
    private final static String TAG = "NewDeckActivity";

    private ArrayList<Deck> deckList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.new_deck_activity);
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

        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createDeck(View V){
        if(isDeckNameBlank(getDeckName()))
            Toast.makeText(this, BLANK_DECK_NAME, Toast.LENGTH_SHORT).show();
        else if(deckNameExists(getDeckName()))
            Toast.makeText(this, DECK_NAME_EXISTS, Toast.LENGTH_SHORT).show();
        else {
            try {
                writeDeckToFile(deckToFile());
                initiateIntent(getDeckName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    //===============================================================================================

    private String getDeckName(){
        EditText deckEditText = (EditText) findViewById(R.id.new_deck_text);
        return deckEditText.getText().toString();
    }

    private Deck deckToFile(){
        Deck deck = new Deck();

        deck.setName(getDeckName());
        return deck;
    }

    private boolean deckNameExists(String userDeckName){
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        try {
            fileInputStream = openFileInput(FILENAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            this.deckList = (ArrayList<Deck>) objectInputStream.readObject();

            for(Deck deck : this.deckList){
                String fileDeckName = deck.getName();
                if(userDeckName.matches(fileDeckName))
                    return true;
            }
        }
        catch (IOException | ClassNotFoundException ex){
            ex.getMessage();
        }
        return false;
        }



    private boolean isDeckNameBlank(String deckName){
        if(deckName.matches(""))
            return true;
        else
            return false;
    }

    private void writeDeckToFile(Deck deck) throws IOException{
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        this.deckList.add(deck);
        objectOutputStream.writeObject(this.deckList);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private void initiateIntent(String deckName){
        Intent intent = new Intent(this, CreateCardActivity.class);
        intent.putExtra(DECKNAME, deckName);
        startActivity(intent);
    }

}