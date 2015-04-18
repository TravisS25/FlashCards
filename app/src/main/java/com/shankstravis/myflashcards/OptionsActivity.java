package com.shankstravis.myflashcards;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.shankstravis.myflashcards.adapters.OptionAdapter;
import com.shankstravis.myflashcards.deck.Consts;
import com.shankstravis.myflashcards.deck.Deck;
import com.shankstravis.myflashcards.deck.DeckHolder;
import com.shankstravis.myflashcards.deck.StudyMode;
import com.shankstravis.myflashcards.filesystem.DeckDatabaseAdapter;
import com.shankstravis.myflashcards.filesystem.DeckReader;
import com.shankstravis.myflashcards.filesystem.DeckWriter;
import com.shankstravis.myflashcards.filesystem.IStorageReader;
import com.shankstravis.myflashcards.filesystem.IStorageWriter;
import com.shankstravis.myflashcards.fragments.DeleteDeckDialog;
import com.shankstravis.myflashcards.fragments.DeleteDeckListener;

/**
 * Created by Travis on 2/8/2015.
 */
public class OptionsActivity extends Activity implements DeleteDeckListener{

    private final static String TAG = "OptionsActivity";
    private String mDeckName;
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

    @Override
    public void deleteDeck(){
        IStorageReader storageReader = new DeckReader(Consts.DECK_FILEPATH);
        ArrayList<Deck> origDeckList, deckList;
        Intent intent;
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(this);
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Object> deckRetrieve = service.submit(storageReader);

        while(true) {
            try {
                if (deckRetrieve.isDone()) {
                    Log.d(TAG, "Future done");
                    origDeckList = new ArrayList<>((ArrayList<Deck>) deckRetrieve.get());
                    deckList = new ArrayList<>(origDeckList);
                    Log.d(TAG, "decklist value: " + deckList);
                    for(Deck deck : deckList){
                        if(deck.getName().equals(mDeckName)){
                            origDeckList.remove(deck);
                            break;
                        }
                    }

                    IStorageWriter storageWriter = new DeckWriter(origDeckList, Consts.DECK_FILEPATH);
                    Thread t1 = new Thread(storageWriter);
                    t1.start();
                    try{
                        t1.join();
                    }
                    catch (InterruptedException ex){
                        ex.printStackTrace();
                    }

                    break;
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }

        String gradeTable = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;
        String deckColumn = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String whereClause = deckColumn + " = ?";
        String[] whereArgs = {mDeckName};
        deckDatabaseAdapter.tableRemove(gradeTable, whereClause, whereArgs);

        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//=================================================================================================

    void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        Deck deck = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition());
        mDeckName = deck.getName();

        mOptions = new ArrayList<>(Arrays.asList("Study Deck", "Edit Deck", "Add Card", "Grades", "Delete"));
        mImages = new ArrayList<>(Arrays.asList(R.drawable.study, R.drawable.edit, R.drawable.add, R.drawable.checkmark, R.drawable.delete));
}

    //Sets up and creates the listview of decks
    private void initiateListAdapter(){

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
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
                    case 4:
                        DeleteDeckDialog deleteDeckDialog = new DeleteDeckDialog();
                        deleteDeckDialog.setCancelable(false);
                        deleteDeckDialog.show(getFragmentManager(), "dialog");
                }
            }
        };

        ListView listView = (ListView) findViewById(R.id.option_listview);
        OptionAdapter optionsAdapter = new OptionAdapter(this, mOptions, mImages);
        listView.setAdapter(optionsAdapter);
        listView.setOnItemClickListener(onItemClickListener);
    }

    private void setDeckName(){
        TextView textView = (TextView) findViewById(R.id.deck_name);
        textView.setText(mDeckName);
    }


}
