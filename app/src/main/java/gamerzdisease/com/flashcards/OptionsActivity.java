package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.DeckHolder;

/**
 * Created by Travis on 2/8/2015.
 */
public class OptionsActivity extends Activity {

    private Intent mIntent;
    private DeckHolder mDeckInfo;
    private final static String TAG = "OptionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.options_activity);
        mDeckInfo = (DeckHolder)getApplication();
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
        mIntent = new Intent(this, StudyDeckActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
    }

    private void initiateAddCardIntent(){
        mIntent = new Intent(this, CreateCardActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
    }

    private void initiateEditDeckIntent(){
        mIntent = new Intent(this, EditDeckTableActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
    }

    private void initiateGradeIntent(){
        mIntent = new Intent(this, GradeActivity.class);
        startActivity(mIntent);
    }

    private void setDeckName(){
        String deckName = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition()).getName();
        TextView textView = (TextView) findViewById(R.id.deck_name);
        textView.setText(deckName);
    }

}
