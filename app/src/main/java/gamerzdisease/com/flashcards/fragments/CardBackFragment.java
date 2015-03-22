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

    public final static String CARD_POSITION = "card position";
    private final static String TAG = "CardBackFragment";
    private DeckHolder mDeckInfo;
    private IAnimation  mAnimationListener;

    public CardBackFragment() {}

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
        mDeckInfo = (DeckHolder) getActivity().getApplication();
    }

    private void initiateAnswer(){
        TextView questionTextView = (TextView)getView().findViewById(R.id.answer);
        ArrayList<Deck> deckList = mDeckInfo.getDeckList();
        int deckPostion = mDeckInfo.getDeckPosition();
        Deck deck = deckList.get(deckPostion);
        int position = StudyMode.getPosition();
        String answerText = deck.getQuestions().get(position);
        questionTextView.setText(answerText);
    }

    private void initiateButtons(){
        BootstrapButton correctButton, incorrectButton, completeButton;
        Button.OnClickListener correctListener, incorrectListener, completeListener;

        correctButton = (BootstrapButton)getView().findViewById(R.id.correct_button);
        incorrectButton = (BootstrapButton)getView().findViewById(R.id.incorrect_button);
        completeButton = (BootstrapButton)getView().findViewById(R.id.complete_button);
        
        correctListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                StudyMode.increasePosition();
                Grade.increaseNumCorrect();
                Log.d(TAG, "Num correct: " + Grade.getNumCorrect());
                mAnimationListener.correctAnswer();
            }
        };

        incorrectListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                StudyMode.increasePosition();
                mAnimationListener.incorrectAnswer();
            }
        };

        completeListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mAnimationListener.complete();
            }
        };

        correctButton.setOnClickListener(correctListener);
        incorrectButton.setOnClickListener(incorrectListener);
        completeButton.setOnClickListener(completeListener);

    }

}
