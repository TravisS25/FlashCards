package gamerzdisease.com.flashcards;

/**
 * Created by Travis on 1/31/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;

public class CreateCardActivity extends Activity {

    private ArrayList<Deck> deckList;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_card_activity);
        setDeckName();
        try {
            initiateDeck();
        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }

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

    @Override
    public void onBackPressed(){
        Toast.makeText(this, Consts.BACK_BUTTON, Toast.LENGTH_SHORT).show();
    }

    public void addCardToDeck(View V){
        if(!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();

    }

    public void toDeckTable(View v){
        initiateIntent();
    }

    //==============================================================================================

    private void setDeckName(){
        Intent intent = getIntent();
        String deckName = intent.getStringExtra(Consts.DECKNAME);
        TextView textView = (TextView) findViewById(R.id.deck_name);
        textView.setText(deckName);
    }

    private String getDeckName(){
        Intent intent = getIntent();
        return intent.getStringExtra(Consts.DECKNAME);
    }

    private boolean isEditBoxesFilled(){

        String questionText = getQuestionEdit();
        String answerText = getAnswerEdit();

        if(questionText.matches("") || answerText.matches(""))
            return false;
        else
            return true;
    }

    private void initiateIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
/*
    private void addCardToDeck() throws IOException, ClassNotFoundException {
        String questionText;
        String answerText;

        for(int i = 0; i < this.deckList.size(); i++){

        }


        this.deckList.add()
    }
*/
    private void initiateDeck() throws IOException, ClassNotFoundException{
        File file;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        String filePath;

        filePath = getFilesDir() + "/" + Consts.FILENAME;
        file = new File(filePath);
        fileInputStream = new FileInputStream(file);
        objectInputStream = new ObjectInputStream(fileInputStream);
        this.deck = new Deck();
        this.deckList = new ArrayList<>();
        this.deckList = (ArrayList<Deck>) objectInputStream.readObject();

    }

    private String getQuestionEdit(){
       EditText questionEdit = (EditText)findViewById(R.id.question_edit);
       return questionEdit.getText().toString();

    }

    private String getAnswerEdit(){
        EditText answerEdit = (EditText)findViewById(R.id.answer_edit);
        return answerEdit.getText().toString();
    }


}
