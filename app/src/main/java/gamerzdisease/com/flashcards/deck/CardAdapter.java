package gamerzdisease.com.flashcards.deck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;

import gamerzdisease.com.flashcards.R;

/**
 * Created by Travis on 2/18/2015.
 */
public class CardAdapter extends BaseAdapter {

    private LinkedHashMap<String, String> cards;
    private String[] keys;

    public CardAdapter(HashMap<String, String> othercards){
        this.cards = new LinkedHashMap<>(othercards);
        this.keys = this.cards.keySet().toArray(new String[othercards.size()]);
    }

    @Override
    public int getCount(){
        return this.cards.size();
    }

    @Override
    public Object getItem(int position){
        return this.cards.get(this.keys[position]);
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
            nameTextView.setText(this.cards.get(this.keys[position]));
            nameTextView.setPadding(0, 0, 10, 0);
            nameTextView.setTextSize(textSize);
            view.addView(nameTextView);
            return view;
        }
        return convertView;
    }
}
