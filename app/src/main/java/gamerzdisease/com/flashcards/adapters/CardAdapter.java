package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.util.TypedValue;
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
public class CardAdapter extends BaseAdapter{

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
        int textSize = (int)context.getResources().getDimension(R.dimen.text_size_edit), questionLength, maxLength = 30;
        String questionText;

        if(convertView == null){
            LinearLayout view = new LinearLayout(context);
            TextView nameTextView = new TextView(context);
            questionLength = mDeck.getQuestions().get(position).trim().length();
            questionText =  mDeck.getQuestions().get(position).trim();
            if(questionText.contains("\n"))
                questionText = questionText.substring(0, questionText.indexOf("\n")) + "...";
            else if(questionLength > maxLength)
                questionText =  mDeck.getQuestions().get(position).trim().substring(0, 29) + "...";

            nameTextView.setText(questionText);
            nameTextView.setPadding(15, 13, 0, 13);
            nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            view.addView(nameTextView);

            return view;
        }
        return convertView;
    }
}
