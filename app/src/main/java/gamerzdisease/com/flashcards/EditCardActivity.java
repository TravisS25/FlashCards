package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.FileWriter;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;

/**
 * Created by Travis on 2/18/2015.
 */

public class EditCardActivity extends Activity {

    private final static String TAG = "EditCardActivity";
    private DeckHolder mDeckInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_card_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        setDeckName();
        setQuestionTextBox();
        setAnswerTextBox();
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
    public void onBackPressed() {
        Toast.makeText(this, Consts.BACK_BUTTON, Toast.LENGTH_SHORT).show();
    }

    public void editCard(View v){
        if (!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            writeToDeckList();
            writeToStorage();
            initiateOptionsIntent();
        }
    }


//==================================================================================================

    private void setDeckName(){
        TextView textView = (TextView)findViewById(R.id.deck_name);
        String deckName = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition()).getName();
        textView.setText(deckName);
    }

    private void setQuestionTextBox(){
        EditText questionEdit = (EditText)findViewById(R.id.question_edit);
        String question = mDeckInfo.getQuestion();
        questionEdit.setText(question);
    }

    private void setAnswerTextBox(){
        EditText questionEdit = (EditText)findViewById(R.id.answer_edit);
        String answer = mDeckInfo.getAnswer();
        questionEdit.setText(answer);
    }

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    //Checks if there is at least one character in both question and answer text boxes
    private boolean isEditBoxesFilled() {
        String questionText = getQuestionEdit();
        String answerText = getAnswerEdit();

        if(questionText.matches("") || answerText.matches(""))
            return false;
        else
            return true;
    }

    //Obtains the text from the question edit box
    private String getQuestionEdit() {
        EditText questionEdit = (EditText) findViewById(R.id.question_edit);
        return questionEdit.getText().toString();

    }

    //Obtains the text from the answer edit box
    private String getAnswerEdit() {
        EditText answerEdit = (EditText)findViewById(R.id.answer_edit);
        return answerEdit.getText().toString();
    }

    //Sets up thread and storage file to write the deck list object
    private void writeToStorage(){
        IStorageWriter storage;
        Thread t1;

        storage = new FileWriter(mDeckInfo.getDeckList(), Consts.DECK_FILEPATH);
        t1 = new Thread(storage);
        t1.start();
    }

    private void writeToDeckList(){
        int deckPosition = mDeckInfo.getDeckPosition();
        Deck deck = mDeckInfo.getDeckList().get(deckPosition);
        ArrayList<String> questions = deck.getQuestions();
        ArrayList<String> answers = deck.getAnswers();
        Log.d(TAG, "Value of questions: " + deck.getQuestions().toString());
        Log.d(TAG, "Value of answers: " + deck.getAnswers().toString());

        int cardPosition = deck.getCardPosition();
        String question = getQuestionEdit();
        String answer = getAnswerEdit();

        questions.set(cardPosition, question);
        answers.set(cardPosition, answer);

        Log.d(TAG, "Value questions after " + deck.getQuestions().toString());
        Log.d(TAG, "Value answer after " + deck.getAnswers().toString());
    }

    //Starts intent to OptionsActivity
    private void initiateOptionsIntent() {
        Intent intent = new Intent(this, EditDeckTableActivity.class);
        startActivity(intent);
    }
}
