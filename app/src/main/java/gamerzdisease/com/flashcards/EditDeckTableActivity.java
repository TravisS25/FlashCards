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

import java.util.HashMap;

import gamerzdisease.com.flashcards.deck.CardAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;

/**
 * Created by Travis on 2/10/2015.
 */
public class EditDeckTableActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private AdapterView.OnItemClickListener onItemClickListener;
    private DeckHolder deckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.edit_deck_table_activity);
        this.deckInfo = (DeckHolder)getApplication();
        initiateListAdapter();
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

//=================================================================================================
    private void initiateListener(){
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), OptionsActivity.class);
                String name = deckInfo.getDeckList().get(position).getName();
                deckInfo.setPosition(position);
                intent.putExtra(Consts.DECKNAME, name);
                startActivity(intent);
            }
        };
    }

    private void initiateListAdapter(){
        ListView listView = (ListView) findViewById(R.id.deck_listview);
        int position = this.deckInfo.getPosition();
        Deck deck = new Deck(this.deckInfo.getDeckList().get(position));
        HashMap<String, String> cards = new HashMap<>(deck.getCards());
        CardAdapter cardAdapter = new CardAdapter(cards);
        listView.setAdapter(cardAdapter);
        initiateListener();
        listView.setOnItemClickListener(this.onItemClickListener);
    }

}
