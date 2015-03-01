package gamerzdisease.com.flashcards.deck;

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

/**
 * Created by Travis on 2/8/2015.
 */
public class DeckAdapter extends BaseAdapter {

    ArrayList<Deck> deckList;
    private final static String TAG = "DeckAdapter";

    public DeckAdapter(ArrayList<Deck> deckList) {
        this.deckList = new ArrayList<>(deckList);
    }

    @Override
    public int getCount(){
        return this.deckList.size();
    }

    @Override
    public Object getItem(int position){
        return this.deckList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();
        int textSize = (int)context.getResources().getDimension(R.dimen.text_size);
        Log.d(TAG,"textsize: "+ textSize);
        if(convertView == null){
            LinearLayout view = new LinearLayout(context);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(deckList.get(position).getName());
            nameTextView.setPadding(0, 0, 10, 0);
            nameTextView.setTextSize(textSize);
            view.addView(nameTextView);
            return view;
        }
        return convertView;
    }

}
