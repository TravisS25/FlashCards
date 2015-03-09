package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 2/18/2015.
 */
public class CardAdapter extends BaseAdapter {

    private Deck mDeck;

    public CardAdapter(Deck deck){
        mDeck = new Deck(deck);
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
        Context context = parent.getContext();
        int textSize = (int)context.getResources().getDimension(R.dimen.text_size);

        if(convertView == null){
            LinearLayout view = new LinearLayout(context);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(mDeck.getQuestions().get(position));
            nameTextView.setPadding(0, 0, 10, 0);
            nameTextView.setTextSize(textSize);
            view.addView(nameTextView);
            return view;
        }
        return convertView;
    }
}
