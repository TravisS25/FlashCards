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
import android.widget.TextView;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import gamerzdisease.com.flashcards.adapters.DeckAdapter;
import gamerzdisease.com.flashcards.adapters.OptionAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.StudyMode;
import gamerzdisease.com.flashcards.filesystem.GradeReader;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;

/**
 * Created by Travis on 2/8/2015.
 */
public class OptionsActivity extends Activity {

    private final static String TAG = "OptionsActivity";
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Intent mIntent;
    private DeckHolder mDeckInfo;
    private ArrayList<String> mOptions;
    private ArrayList<Integer> mImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.options_activity);
        initiateObjects();
        initiateListAdapter();
        setDeckName();
        test();
        Log.d(TAG, "StudyMode position: " + StudyMode.getPosition());
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

    void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        mOptions = new ArrayList<>(Arrays.asList("Study Deck", "Edit Deck", "Add Card", "Grades"));
        mImages = new ArrayList<>(Arrays.asList(R.drawable.study, R.drawable.edit, R.drawable.add, R.drawable.checkmark));
    }

    //Initializes onclicklistener for listview
    private void initiateListener(){
        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        intent = new Intent(parent.getContext(), StudyDeckActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(parent.getContext(), EditDeckTableActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(parent.getContext(), CreateCardActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(parent.getContext(), GradeActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        };
    }

    //Sets up and creates the listview of decks
    private void initiateListAdapter(){
        ListView listView = (ListView) findViewById(R.id.option_listview);
        OptionAdapter optionsAdapter = new OptionAdapter(this, mOptions, mImages);
        listView.setAdapter(optionsAdapter);
        initiateListener();
        listView.setOnItemClickListener(mOnItemClickListener);
    }

    private void initiateStudyDeckIntent(){
        mIntent = new Intent(this, StudyDeckActivity.class);
        startActivity(mIntent);
    }

    private void initiateAddCardIntent(){
        mIntent = new Intent(this, CreateCardActivity.class);
        startActivity(mIntent);
    }

    private void initiateEditDeckIntent(){
        mIntent = new Intent(this, EditDeckTableActivity.class);
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

    private void test(){
        HashMap<String, Integer> grades;
        IStorageReader reader = new GradeReader(Consts.DECK_POSITION_FILEPATH);
        try {
            grades = new HashMap<>((HashMap<String, Integer>)reader.readFromStorage());
            Log.d(TAG, "Deck position: " + grades);
        }
        catch (EOFException ex) {
            Log.d(TAG, "EOFException reached");
            grades = new HashMap<>();
        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }

    }
}
