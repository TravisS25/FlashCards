package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import gamerzdisease.com.flashcards.deck.Consts;

/**
 * Created by Travis on 2/8/2015.
 */
public class OptionsActivity extends Activity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.options_activity);

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
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void studyDeck(View v){
        initiateStudyDeckIntent();
    }

    public void editDeck(View V){initiateEditDeckIntent();}

    public void addCard(View v){
        initiateAddCardIntent();
    }

    public void grades(View v){initiateGradeIntent();}



//=================================================================================================
    private void initiateStudyDeckIntent(){
        this.intent = new Intent(this, StudyDeckActivity.class);
        startActivity(intent);
    }

    private void initiateAddCardIntent(){
        this.intent = new Intent(this, CreateCardActivity.class);
        startActivity(intent);
    }

    private void initiateEditDeckIntent(){
        this.intent = new Intent(this, EditDeckTableActivity.class);
        startActivity(this.intent);
    }

    private void initiateGradeIntent(){
        this.intent = new Intent(this, GradeActivity.class);
        startActivity(intent);
    }

    private void setDeckName(){
        this.intent = getIntent();
        String deckName = intent.getStringExtra(Consts.DECKNAME);
        TextView textView = (TextView) findViewById(R.id.deck_name);
        textView.setText(deckName);
    }

}
