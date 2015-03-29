package gamerzdisease.com.flashcards.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.Grade;
import gamerzdisease.com.flashcards.deck.StudyMode;

/**
 * Created by Travis on 3/14/2015.
 */
public class CardBackFragment extends Fragment {

    private final static String TAG = "CardBackFragment";
    private Deck mDeck;
    private CardFragmentActivity mAnimationListener;
    private Button.OnClickListener mCorrectListener, mIncorrectListener, mCompleteListener;

    public CardBackFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mAnimationListener = (CardFragmentActivity) activity;
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
        return inflater.inflate(R.layout.fragment_card_back, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initiateButtons();
        initiateAnswer();
    }

//==================================================================================================

    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder) getActivity().getApplication();
        ArrayList<Deck> deckList = deckInfo.getDeckList();
        int deckPostion = deckInfo.getDeckPosition();
        mDeck = deckList.get(deckPostion);
    }

    private void initiateAnswer(){
        TextView questionTextView = (TextView)getView().findViewById(R.id.answer);
        int position = StudyMode.getPosition();
        String answerText = mDeck.getAnswers().get(position);
        questionTextView.setText(answerText);
    }

    private void initiateButtons(){
        BootstrapButton correctButton, incorrectButton, completeButton;

        initiateButtonListeners();
        correctButton = (BootstrapButton)getView().findViewById(R.id.correct_button);
        incorrectButton = (BootstrapButton)getView().findViewById(R.id.incorrect_button);
        completeButton = (BootstrapButton)getView().findViewById(R.id.complete_button);

        correctButton.setOnClickListener(mCorrectListener);
        incorrectButton.setOnClickListener(mIncorrectListener);
        completeButton.setOnClickListener(mCompleteListener);
    }

    private void initiateButtonListeners(){
        mCorrectListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                StudyMode.increasePosition();
                Grade.increaseNumCorrect();
                Log.d(TAG, "Num correct: " + Grade.getNumCorrect());
                mAnimationListener.correctAnswer();
            }
        };

        mIncorrectListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                StudyMode.increasePosition();
                mAnimationListener.incorrectAnswer();
            }
        };

        mCompleteListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mAnimationListener.complete();
            }
        };
    }

}
