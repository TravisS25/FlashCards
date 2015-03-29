package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.Grade;
import gamerzdisease.com.flashcards.deck.StudyMode;
import gamerzdisease.com.flashcards.filesystem.GradeReader;
import gamerzdisease.com.flashcards.filesystem.GradeWriter;
import gamerzdisease.com.flashcards.filesystem.IStorageReader;
import gamerzdisease.com.flashcards.filesystem.IStorageWriter;

/**
 * Created by Travis on 3/20/2015.
 */
public class DeckScoreActivity extends Activity {

    private final static String TAG = "DeckScoreActivity";
    DeckHolder mDeckInfo;
    HashMap<String, ArrayList<Double>> mGradeList;
    ArrayList<Double> mGrades;
    double mGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finished_deck);
        initiateObjects();
        displayGrade();
        readGradeFromStorage();
        writeGradeToStorage();
        reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void toOptionsActivity(View v){
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


//================================================================================================

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    private void displayGrade(){
        TextView gradePercentage = (TextView)findViewById(R.id.deck_percentage);
        TextView gradeSize = (TextView)findViewById(R.id.deck_size);
        Deck deck = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition());
        int deckSize = deck.getQuestions().size();

        mGrade = Grade.calculateGrade(deckSize);
        String percentage = String.format("%.1f", mGrade) + "%";
        String gradeCount = String.valueOf(Grade.getNumCorrect()) + "/" + String.valueOf(deckSize);

        gradePercentage.setText(percentage);
        gradeSize.setText(gradeCount);
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

    private void writeGradeToStorage(){
        IStorageWriter gradeWriter;
        Thread t;
        Deck deck;
        String deckName;
        boolean flag = false;

        deck = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition());
        deckName = deck.getName();

        for(String key : mGradeList.keySet()){
            if(key.equals(deckName))
                flag = true;
        }

        if(flag)
            mGrades = new ArrayList<>(mGradeList.get(deckName));
        else
            mGrades = new ArrayList<>();

        Log.d(TAG, "Value of grades: " + mGrades);
        Log.d(TAG, "Keys of gradelist: " + mGradeList.keySet());
        mGrades.add(mGrade);
        Log.d(TAG, "Value of mGrades: " + mGrades);
        mGradeList.put(deckName, mGrades);
        gradeWriter = new GradeWriter(mGradeList, Consts.GRADE_FILEPATH);
        t = new Thread(gradeWriter);
        t.start();
    }

    private void reset(){
        StudyMode.resetPosition();
        Grade.resetCorrect();
    }


}
