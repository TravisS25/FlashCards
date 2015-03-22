package gamerzdisease.com.flashcards.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.StudyDeckActivity;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.Grade;
import gamerzdisease.com.flashcards.deck.StudyMode;

/**
 * Created by Travis on 3/14/2015.
 */
public class CardFrontFragment extends Fragment {

    public final static String CARD_POSITION = "card position";
    public final static String IS_FINISHED = "finished";
    private final static String TAG = "CardFrontFragment";
    private DeckHolder mDeckInfo;
    private Deck mDeck;
    private IAnimation mAnimationListener;

    public CardFrontFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mAnimationListener = (IAnimation) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IAnimation");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initiateObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mDeck.getQuestions().size() == 0)
            return inflater.inflate(R.layout.no_card, container, false);
        else
            return inflater.inflate(R.layout.fragment_card_front, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(mDeck.getQuestions().size() != 0) {
            initiateQuestionText();
            initiateButton();
        }
    }


//==================================================================================================

    private void initiateQuestionText(){
        TextView questionTextView = (TextView)getView().findViewById(R.id.question);
        ArrayList<Deck> deckList = mDeckInfo.getDeckList();
        int deckPostion = mDeckInfo.getDeckPosition();
        Deck deck = deckList.get(deckPostion);
        int position = StudyMode.getPosition();
        String questionText = deck.getQuestions().get(position);
        questionTextView.setText(questionText);
    }

    private void initiateObjects(){
        mDeckInfo = (DeckHolder) getActivity().getApplication();

        ArrayList<Deck> deckList = mDeckInfo.getDeckList();
        int deckPostion = mDeckInfo.getDeckPosition();
        mDeck = deckList.get(deckPostion);
    }

    private void initiateButton(){
        BootstrapButton answerButton;
        Button.OnClickListener onclickListener;

        onclickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mAnimationListener.flipCardBack();
            }
        };

        answerButton = (BootstrapButton)getView().findViewById(R.id.answer_button);
        answerButton.setOnClickListener(onclickListener);
    }

}

