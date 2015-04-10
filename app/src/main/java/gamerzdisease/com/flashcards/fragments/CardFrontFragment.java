package gamerzdisease.com.flashcards.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.HashMap;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;
import gamerzdisease.com.flashcards.deck.StudyMode;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;


/**
 * Created by Travis on 3/14/2015.
 */
public class CardFrontFragment extends Fragment implements DialogClickListener {

    private final static String TAG = "CardFrontFragment";
    private Deck mDeck;
    private HashMap<String, Integer> mDeckPosition;
    private HashMap<String, Boolean> mIsFirstStudy;
    private CardFragmentActivity mAnimationListener;

    public CardFrontFragment() {}

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
        Log.d(TAG, "OnCreate Called");
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
            if(isPreviousStudy()) {
                Log.d(TAG, "Made it to previous study");
                displayDialog();
            }
            else {
                Log.d(TAG, "Made it to else statement");
                initiateQuestionText();
                initiateButton();
            }
        }
    }

    @Override
    public void onContinueClick(HashMap<String, Integer> deckPosition){
        continueFromLastQuestion(deckPosition);
        initiateButton();
    }

    @Override
    public void onRestartClick(){
        initiateQuestionText();
        initiateButton();
    }


//==================================================================================================

    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder) getActivity().getApplication();
        ArrayList<Deck> deckList = deckInfo.getDeckList();
        int deckPosition = deckInfo.getDeckPosition();
        mDeck = deckList.get(deckPosition);
    }

    private boolean isPreviousStudy(){
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String[] columns = {deckColumnName};
        String selection = deckColumnName + " = ?";
        String[] selectionArgs = {mDeck.getName()};
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(getActivity());
        Cursor cursor = deckDatabaseAdapter.tableQuery(tableName, columns, selection, selectionArgs, null, null, null, null);
        return cursor.moveToFirst();
    }

    private int removeDeckFromTable(){
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String whereClause = deckColumnName + " = ?";
        String[] whereArgs = {mDeck.getName()};
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(getActivity());
        return deckDatabaseAdapter.tableRemove(tableName, whereClause, whereArgs);
    }

    private int getStudyPosition(){
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String studyPositionColumnName = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        String[] columns = {deckColumnName, studyPositionColumnName};
        String selection = deckColumnName + " = ?";
        String[] selectionArgs = {mDeck.getName()};
        DeckDatabaseAdapter deckDatabaseAdapter = new DeckDatabaseAdapter(getActivity());
        Cursor cursor = deckDatabaseAdapter.tableQuery(tableName, columns, selection, selectionArgs, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(studyPositionColumnName));
    }
    private void displayDialog(){
        Bundle bundle = new Bundle();
        String deckName = mDeck.getName();
        int position = getStudyPosition();
        removeDeckFromTable();
        bundle.putInt(Consts.DECK_POSITION, position);
        bundle.putString(Consts.DECK_NAME, deckName);

        DialogFragment dialogFragment = new ContinueRestartDialog();
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    private void initiateQuestionText(){
        TextView questionTextView = (TextView)getView().findViewById(R.id.question);
        int position = StudyMode.getPosition();
        Log.d(TAG, "Studymode position: " + position);
        String questionText = mDeck.getQuestions().get(position);
        Log.d(TAG, "Question text: " + questionText);
        questionTextView.setText(questionText);
    }

    private void continueFromLastQuestion(HashMap<String, Integer> deckPosition){
        TextView questionTextView = (TextView)getView().findViewById(R.id.question);
        Log.d(TAG, "Value of Deck Position" + deckPosition);
        Log.d(TAG, "Value of Deck name" + mDeck.getName());
        int position = deckPosition.get(mDeck.getName());
        Log.d(TAG, "Question text from continue: " + position);
        String questionText = mDeck.getQuestions().get(position);
        StudyMode.setPosition(position);
        questionTextView.setText(questionText);
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

