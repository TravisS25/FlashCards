package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import gamerzdisease.com.flashcards.adapters.CardAdapter;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;

/**
 * Created by Travis on 2/10/2015.
 */
public class EditDeckTableActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private DeckHolder mDeckInfo;
    private Deck mDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        if(mDeck.getQuestions().size() == 0)
            setContentView(R.layout.no_edit_card);
        else {
            setContentView(R.layout.edit_deck_table_activity);
            initiateListAdapter(mDeck);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigateUp () {
        onBackPressed();
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

//=================================================================================================

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        ArrayList<Deck> deckList = mDeckInfo.getDeckList();
        int position = mDeckInfo.getDeckPosition();
        mDeck = deckList.get(position);
    }

    private void initiateListAdapter(Deck deck){
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), EditCardActivity.class);

                int deckPosition = mDeckInfo.getDeckPosition();
                Deck deck = mDeckInfo.getDeckList().get(deckPosition);
                deck.setCardPosition(position);

                ArrayList<String> questions = new ArrayList<>(deck.getQuestions());
                ArrayList<String> answers = new ArrayList<>(deck.getAnswers());
                String question = questions.get(position);
                String answer = answers.get(position);
                mDeckInfo.setQuestion(question);
                mDeckInfo.setAnswer(answer);
                startActivity(intent);
            }
        };

        ListView listView = (ListView) findViewById(R.id.deck_listview);
        CardAdapter cardAdapter = new CardAdapter(this, deck);
        listView.setAdapter(cardAdapter);
        listView.setOnItemClickListener(onItemClickListener);
    }

}
