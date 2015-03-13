package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 2/8/2015.
 */
public class DeckAdapter extends BaseAdapter {

    ArrayList<Deck> mDeckList;
    private final static String TAG = "DeckAdapter";

    public DeckAdapter(ArrayList<Deck> deckList) {
        mDeckList = new ArrayList<>(deckList);
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
        int color = 0xFFC9C6C7;
        Context context = parent.getContext();
        int textSize = (int)context.getResources().getDimension(R.dimen.text_size);
        if(convertView == null){
            LinearLayout view = new LinearLayout(context);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(mDeckList.get(position).getName().trim());
            nameTextView.setPadding(10, 10, 0, 10);
            nameTextView.setTextSize(textSize);
            view.addView(nameTextView);
            if(position % 2 == 0)
                view.setBackgroundColor(color);
            return view;
        }
        return convertView;
    }

}
