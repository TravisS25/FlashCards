package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 2/8/2015.
 */
public class DeckAdapter extends BaseAdapter {

    ArrayList<Deck> mDeckList;
    HashMap<String, ArrayList<Double>> mGradeList;
    private final static String TAG = "DeckAdapter";

    public DeckAdapter(ArrayList<Deck> deckList, HashMap<String, ArrayList<Double>> gradeList ) {
        mDeckList = new ArrayList<>(deckList);
        mGradeList = new HashMap<>(gradeList);
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
        Context context = parent.getContext();
        int deckTextSize = (int)context.getResources().getDimension(R.dimen.text_size_special);
        int gradeTextSize = (int)context.getResources().getDimension(R.dimen.text_size_grade);
        int cardNumTextSize = (int)context.getResources().getDimension(R.dimen.text_size_small);
        Deck deck = mDeckList.get(position);
        String deckName = deck.getName().trim();
        Set<String> deckSetName = mGradeList.keySet();
        ArrayList<Double> grades;
        double grade;

        if(convertView == null){
            RelativeLayout relativeLayout = new RelativeLayout(parent.getContext());
            TextView deckNameTextView = new TextView(parent.getContext());
            TextView gradeTextView = new TextView(parent.getContext());
            TextView deckCardNumTextView = new TextView(parent.getContext());

            deckNameTextView.setId(R.id.deck);
            gradeTextView.setId(R.id.grade);
            deckCardNumTextView.setId(R.id.deck_card_number);

            deckNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,deckTextSize);
            gradeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, gradeTextSize);
            gradeTextView.setPadding(0, 40, 0, 0);
            deckCardNumTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, cardNumTextSize);

            deckNameTextView.setText(deckName);
            if(deckSetName.contains(deckName)) {
                grades = new ArrayList<>(mGradeList.get(deckName));
                grade = Collections.max(grades);
                gradeTextView.setText(String.format("%.1f", grade) + "%");
            }
            if(deck.getQuestions().size() > 0)
                deckCardNumTextView.setText("Cards: " + String.valueOf(deck.getQuestions().size()));

            RelativeLayout.LayoutParams deckNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            deckNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            deckNameTextView.setLayoutParams(deckNameParams);

            RelativeLayout.LayoutParams gradeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            gradeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            gradeTextView.setLayoutParams(gradeParams);

            RelativeLayout.LayoutParams deckCardNumParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            deckCardNumParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            deckCardNumParams.addRule(RelativeLayout.BELOW, R.id.deck);
            deckCardNumTextView.setLayoutParams(deckCardNumParams);

            relativeLayout.addView(deckNameTextView);
            relativeLayout.addView(gradeTextView);
            relativeLayout.addView(deckCardNumTextView);


            return relativeLayout;
        }

        return convertView;
    }

}
