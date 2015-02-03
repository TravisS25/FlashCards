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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.create_card_activity);
        setDeckName();
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

    public void addCardToDeck(){
        if(checkTextBoxes()){

        }
    }

    //==============================================================================================

    private void setDeckName(){
        Intent intent = getIntent();
        String deckName = intent.getStringExtra(NewDeckActivity.DECKNAME);
        TextView textView = (TextView) findViewById(R.id.deck_name);
        textView.setText(deckName);
    }

    private boolean checkTextBoxes(){
        String message = "You must enter text in the question and answer textbox";
        EditText questionEdit = (EditText)findViewById(R.id.question_edit);
        EditText answerEdit = (EditText)findViewById(R.id.answer_edit);

        String questionText = questionEdit.getText().toString();
        String answerText = answerEdit.getText().toString();

        if(questionText.matches("") || answerText.matches("")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }
}
