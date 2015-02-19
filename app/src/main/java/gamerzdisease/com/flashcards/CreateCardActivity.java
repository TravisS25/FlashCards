package gamerzdisease.com.flashcards;

/**
 * Created by Travis on 1/31/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import gamerzdisease.com.flashcards.deck.DeckHolder;

public class CreateCardActivity extends Activity {

    private DeckHolder deckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_card_activity);
        setDeckName();
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
        else {
            this.deckInfo.addCardToDeck(getQuestionEdit(), getAnswerEdit());
            clearEditBoxes();
        }

    }

    public void toDeckTable(View v){
        try {
            this.deckInfo.writeDeckToFile();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        initiateMainActivityIntent();
    }

    //==============================================================================================

    private void setDeckName(){
        Intent intent = getIntent();
        String deckName = intent.getStringExtra(Consts.DECKNAME);
        TextView textView = (TextView) findViewById(R.id.deck_name);
        textView.setText(deckName);
    }

    private boolean isEditBoxesFilled(){

        String questionText = getQuestionEdit();
        String answerText = getAnswerEdit();

        if(questionText.matches("") || answerText.matches(""))
            return false;
        else
            return true;
    }

    private void initiateMainActivityIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private String getQuestionEdit(){
       EditText questionEdit = (EditText)findViewById(R.id.question_edit);
       return questionEdit.getText().toString();

    }

    private String getAnswerEdit(){
        EditText answerEdit = (EditText)findViewById(R.id.answer_edit);
        return answerEdit.getText().toString();
    }

    private void clearEditBoxes(){
        EditText questionEdit = (EditText)findViewById(R.id.question_edit);
        questionEdit.setText("");
        EditText answerEdt = (EditText)findViewById(R.id.answer_edit);
        answerEdt.setText("");
    }

}
