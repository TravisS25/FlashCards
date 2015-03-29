package gamerzdisease.com.flashcards.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.HashMap;

import gamerzdisease.com.flashcards.deck.Consts;

/**
 * Created by Travis on 3/23/2015.
 */
public class YesNoDialog extends DialogFragment {

    private AlertDialog.OnClickListener mRestartListener, mContinueListener;
    private DialogClickListener mCallback;
    private HashMap<String, Integer> mDeckPosition;

    public YesNoDialog(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String deckName = bundle.getString(Consts.DECK_NAME);
        int position = bundle.getInt(Consts.DECK_POSITION);
        mDeckPosition = new HashMap<>();
        mDeckPosition.put(deckName, position);

        try {
            mCallback = (DialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        initiateAlertListeners();
        String title = "Previous Study Session";
        String message = "You exited early from previous study.  Do you wish to continue or restart?";
        String restart = "Restart";
        String start = "Continue";

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(start, mContinueListener)
                .setNegativeButton(restart, mRestartListener)
                .create();
    }

    private void initiateAlertListeners(){
        mRestartListener = new AlertDialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1){
                mCallback.onRestartClick();
            }
        };

        mContinueListener = new AlertDialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1){
                mCallback.onContinueClick(mDeckPosition);
            }
        };
    }
}
