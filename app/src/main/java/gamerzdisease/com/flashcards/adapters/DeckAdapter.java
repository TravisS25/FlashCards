package gamerzdisease.com.flashcards.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;

/**
 * Created by Travis on 2/8/2015.
 */
public class DeckAdapter extends BaseAdapter {

    private final static String TAG = "DeckAdapter";
    ArrayList<Deck> mDeckList;
    ArrayList<String> mDecks;
    ArrayList<Double> mGrades;
    private static LayoutInflater mInflate;

    public DeckAdapter(Activity mainActivity, ArrayList<Deck> deckList, ArrayList<String> decks, ArrayList<Double> grades){
        mDeckList = new ArrayList<>(deckList);
        mDecks = new ArrayList<>(decks);
        mGrades = new ArrayList<>(grades);
        mInflate = (LayoutInflater)mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mDeckList.size();
    }

    @Override
    public Object getItem(int position){
        return mDeckList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView;

        if(convertView == null){
            rowView = mInflate.inflate(R.layout.main_activity_listview, null);
            TextView deckTextView = (TextView)rowView.findViewById(R.id.deck_name);
            TextView numOfCardsTextView = (TextView)rowView.findViewById(R.id.number_of_cards);
            TextView gradeTextView = (TextView)rowView.findViewById(R.id.grade);

            String deckText = mDeckList.get(position).getName();
            int numOfGradesText = mDeckList.get(position).getQuestions().size();
            deckTextView.setText(deckText);
            numOfCardsTextView.setText(String.valueOf(numOfGradesText));
            for(String deckName : mDecks){
                if(deckText.equals(deckName)){
                    double grade = mGrades.get(position);
                    gradeTextView.setText(String.valueOf(String.valueOf(grade)));
                }
            }
            return rowView;
        }
        return convertView;
    }

}
