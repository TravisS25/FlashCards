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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.DeckWriter;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;

public class CreateCardActivity extends Activity {

    private final static String TAG = "CreateCardActivity";
    private DeckHolder mDeckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.create_card_activity);
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

    //Activated when user clicks "Add Card" button
    //Contains checks for question and answer text boxes
    public void addCard(View V) {
        if (!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            Deck deck = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition());
            deck.setQuestion(getQuestionEdit());
            deck.setAnswer(getAnswerEdit());
            Log.d(TAG, mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition()).toString());
            clearEditBoxes();
        }

    }

    // Activates when user clicks "Done" button
    // Starts thread and writes deck list to storage and to application variable mDeckInfo
    // Starts activity to OptionsActivity
    public void writeToList(View v) {
        writeToStorage();
        initiateOptionsIntent();
    }

    //Lowers the keyboard whenever the user clicks off of the textboxes
    public void keyBoard(View v){
        if(findViewById(R.id.question_edit).isFocused() || findViewById(R.id.answer_edit).isFocused())
            hideKeyBoard(v);
    }

    //==============================================================================================

    //Initializes class objects
    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    //Lowers the keyboard
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

    //Starts intent to OptionsActivity
    private void initiateOptionsIntent() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    //Obtains the text from the question edit box
    private String getQuestionEdit() {
        EditText questionEdit = (EditText) findViewById(R.id.question_edit);
        return questionEdit.getText().toString();
    }

    //Obtains the text from the answer edit box
    private String getAnswerEdit() {
        EditText answerEdit = (EditText) findViewById(R.id.answer_edit);
        return answerEdit.getText().toString();
    }

    //Clears both text boxes when user clicks "Add Card" button
    private void clearEditBoxes() {
        EditText questionEdit = (EditText)findViewById(R.id.question_edit);
        questionEdit.setText("");
        EditText answerEdt = (EditText)findViewById(R.id.answer_edit);
        answerEdt.setText("");
    }

    //Sets up thread and storage file to write the deck list object
    private void writeToStorage(){
        IStorageWriter storage;
        Thread t1;

        storage = new DeckWriter(mDeckInfo.getDeckList(), Consts.DECK_FILEPATH);
        t1 = new Thread(storage);
        t1.start();
        try {
            t1.join();
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

}


