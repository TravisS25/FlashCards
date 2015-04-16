package gamerzdisease.com.flashcards.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Deck;

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

        if(convertView == null)
            rowView = mInflate.inflate(R.layout.main_activity_listview, null);
        else
            rowView = convertView;

        TextView deckTextView = (TextView)rowView.findViewById(R.id.deck_name);
        TextView numOfCardsTextView = (TextView)rowView.findViewById(R.id.number_of_cards);

        String deckText = mDeckList.get(position).getName();
        int numOfGradesText = mDeckList.get(position).getQuestions().size();
        deckTextView.setText(deckText);
        numOfCardsTextView.setText("Cards: " + String.valueOf(numOfGradesText));

        return rowView;
    }

}
