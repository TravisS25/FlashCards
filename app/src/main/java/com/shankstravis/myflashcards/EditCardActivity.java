package com.shankstravis.myflashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import com.shankstravis.myflashcards.deck.Consts;
import com.shankstravis.myflashcards.deck.Deck;
import com.shankstravis.myflashcards.deck.DeckHolder;
import com.shankstravis.myflashcards.filesystem.DeckWriter;
import com.shankstravis.myflashcards.filesystem.IStorageWriter;

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

    //The "Done" button that saves the changes to the card
    public void editCard(View v){
        if (!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            writeToDeckList();
            writeToStorage();
            initiateOptionsIntent();
        }
    }

    //Hides the keyboard when user clicks outside the textboxes
    public void editKeyBoard(View v){
        if(findViewById(R.id.question_edit).isFocused() || findViewById(R.id.answer_edit).isFocused())
            hideKeyBoard(v);
    }


//==================================================================================================

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    //Displays the text to edit the question in a textbox
    private void setQuestionTextBox(){
        EditText questionEdit = (EditText)findViewById(R.id.question_edit);
        String question = mDeckInfo.getQuestion();
        questionEdit.setText(question);
    }

    //Displays the text to edit the answer in a textbox
    private void setAnswerTextBox(){
        EditText questionEdit = (EditText)findViewById(R.id.answer_edit);
        String answer = mDeckInfo.getAnswer();
        questionEdit.setText(answer);
    }

    //If user clicks off textboxes will lower keyboard
    private void hideKeyBoard(View view){
        Log.d(TAG, "Made to hide keyboard");
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

        storage = new DeckWriter(mDeckInfo.getDeckList(), Consts.DECK_FILEPATH);
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
