package com.shankstravis.myflashcards.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shankstravis.myflashcards.R;
import com.shankstravis.myflashcards.deck.Deck;

/**
 * Created by Travis on 2/18/2015.
 */
public class CardAdapter extends BaseAdapter{

    private Deck mDeck;
    private static LayoutInflater mInflate;

    public CardAdapter(Activity editDeckTableActivity, Deck deck){
        mDeck = new Deck(deck);
        mInflate = (LayoutInflater)editDeckTableActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mDeck.getAnswers().size();
    }

    @Override
    public Object getItem(int position){
        return mDeck.getAnswers();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView;
        int maxLength = 30;

        if(convertView == null)
            rowView = mInflate.inflate(R.layout.edit_card_listview, null);
        else
            rowView = convertView;

        int questionLength = mDeck.getQuestions().get(position).trim().length();
        String questionText =  mDeck.getQuestions().get(position).trim();
        if(questionText.contains("\n"))
            questionText = questionText.substring(0, questionText.indexOf("\n")) + "...";
        else if(questionLength > maxLength)
            questionText =  mDeck.getQuestions().get(position).trim().substring(0, 29) + "...";

        TextView questionTextView = (TextView)rowView.findViewById(R.id.edit_card);
        questionTextView.setText(questionText);
        return rowView;
    }
}
