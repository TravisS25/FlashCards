package com.shankstravis.myflashcards.filesystem;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.shankstravis.myflashcards.deck.Deck;

/**
 * Created by Travis on 3/4/2015.
 */
public class DeckReader implements IStorageReader {

    private final static String TAG = "DeckReader";
    private File mDeckFile;

    public DeckReader(String deckFile) { mDeckFile = new File(deckFile); }

    public ArrayList<Deck> call(){
        try{
            return readFromStorage();
        }
        catch (EOFException ex){
            Log.d(TAG, "EOFException reached");
            return new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
            ex.getMessage();
            Log.d(TAG, "Reached exception");
            return null;
        }
    }

    public ArrayList<Deck> readFromStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        ArrayList<Deck> deckList;

        fileInputStream = new FileInputStream(mDeckFile);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        deckList = new ArrayList<>((ArrayList<Deck>) objectInputStream.readObject());
        objectInputStream.close();
        fileInputStream.close();
        return deckList;
    }

    public void initiateStorage() throws IOException{
        if (mDeckFile.createNewFile())
            Log.d(TAG, "Created deck file");
        else
            Log.d(TAG, "Deck file already exists");
    }

}
