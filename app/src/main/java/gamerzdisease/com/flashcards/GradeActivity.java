package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gamerzdisease.com.flashcards.adapters.GradeAdapter;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.filesystem.GradeReader;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;

/**
 * Created by Travis on 2/13/2015.
 */
public class GradeActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private HashMap<String, ArrayList<Double>> mGradeList;
    private String mDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        readGradeFromStorage();
        Log.d(TAG, "mGradeList size: " + mGradeList.size());
        if(isGradeForDeck()) {
            setContentView(R.layout.grades_activity);
            initiateListAdapter();
        }
        else
            setContentView(R.layout.no_grade);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//=================================================================================================

    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder)getApplication();
        Deck deck = deckInfo.getDeckList().get(deckInfo.getDeckPosition());
        mDeckName = deck.getName();
    }

    private boolean isGradeForDeck(){
        for(String key : mGradeList.keySet()){
            if(key.equals(mDeckName))
                return true;
        }
        return false;
    }

    private void readGradeFromStorage(){
        IStorageReader gradeReader;
        ExecutorService service;
        Future<Object> future;

        gradeReader = new GradeReader(Consts.GRADE_FILEPATH);
        service = Executors.newFixedThreadPool(6);
        future = service.submit(gradeReader);

        while(true){
            try{
                if(future.isDone()){
                    mGradeList = new HashMap<>((HashMap<String, ArrayList<Double>>)future.get());
                    return;
                }
            }
            catch (InterruptedException | ExecutionException ex){
                ex.printStackTrace();
            }
        }
    }

    private void initiateListAdapter(){
        ListView listView;
        GradeAdapter gradeAdapter;
        ArrayList<Double> grades;

        listView = (ListView)findViewById(R.id.grade_listview);
        grades = new ArrayList<>(mGradeList.get(mDeckName));
        gradeAdapter = new GradeAdapter(grades);
        listView.setAdapter(gradeAdapter);
    }
}


