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

public class NewDeckActivity extends Activity {

    private final static String DECK_NAME_EXISTS = "That deck name already exists";
    private final static String DECK_CHARACTERS = "Deck name must contain at least one character";
    private final static String TAG = "NewDeckActivity";
    private ArrayList<Deck> deckList;
    private File file;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.new_deck_activity);
        this.deckList = new ArrayList<>();
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
            Toast.makeText(this, DECK_NAME_EXISTS, Toast.LENGTH_SHORT).show();
        else if(!doesDeckHaveCharacters(getDeckName()))
            Toast.makeText(this, DECK_CHARACTERS, Toast.LENGTH_SHORT).show();
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
            String filePath = getFilesDir() + "/" + Consts.FILENAME;
            this.file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            try{
                this.deckList = (ArrayList<Deck>) objectInputStream.readObject();

                for (Deck deck : this.deckList) {
                    String fileDeckName = deck.getName();
                    if (userDeckName.matches(fileDeckName))
                        return true;
                }
            }
            finally {
                fileInputStream.close();
                objectInputStream.close();
            }

        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
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

    private void writeDeckToFile(Deck deck) throws IOException{
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        this.deckList.add(deck);
        fileOutputStream = new FileOutputStream(this.file);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this.deckList);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    private void initiateIntent(String deckName){
        Intent intent = new Intent(this, CreateCardActivity.class);
        intent.putExtra(Consts.DECKNAME, deckName);
        startActivity(intent);
    }

}