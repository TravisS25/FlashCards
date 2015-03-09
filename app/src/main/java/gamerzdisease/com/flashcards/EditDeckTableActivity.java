package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gamerzdisease.com.flashcards.adapters.CardAdapter;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;

/**
 * Created by Travis on 2/10/2015.
 */
public class EditDeckTableActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DeckHolder mDeckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.edit_deck_table_activity);
        mDeckInfo = (DeckHolder)getApplication();
        initiateListAdapter();
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
    private void initiateListener(){
        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), EditCardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                int deckPosition = mDeckInfo.getDeckPosition();
                Log.d(TAG, "deck position: " + String.valueOf(deckPosition));
                Deck deck = mDeckInfo.getDeckList().get(deckPosition);
                deck.setCardPosition(position);
                Log.d(TAG, "card position: " + String.valueOf(deck.getCardPosition()));

                ArrayList<String> questions = new ArrayList<>(deck.getQuestions());
                ArrayList<String> answers = new ArrayList<>(deck.getAnswers());
                String question = questions.get(position);
                String answer = answers.get(position);
                mDeckInfo.setQuestion(question);
                mDeckInfo.setAnswer(answer);
                startActivity(intent);
            }
        };
    }

    private void initiateListAdapter(){
        ListView listView = (ListView) findViewById(R.id.deck_listview);
        int position = mDeckInfo.getDeckPosition();
        Deck deck = new Deck(mDeckInfo.getDeckList().get(position));
        CardAdapter cardAdapter = new CardAdapter(deck);
        listView.setAdapter(cardAdapter);
        initiateListener();
        listView.setOnItemClickListener(mOnItemClickListener);
    }

}
