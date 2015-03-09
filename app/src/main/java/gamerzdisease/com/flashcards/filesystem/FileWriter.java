package gamerzdisease.com.flashcards.filesystem;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gamerzdisease.com.flashcards.deck.Deck;


/**
 * Created by Travis on 3/2/2015.
 */
public class FileWriter implements IStorageWriter {

    private final static String TAG = "FileWriter";
    private ArrayList<Deck> mDeckList;
    private Deck mDeck;
    private File mDeckFile, mGradeFile;

    public FileWriter(ArrayList<Deck> deckList) {
        mDeckList = new ArrayList<>(deckList);
    }

    public FileWriter(ArrayList<Deck> deckList, Deck deck) {
        mDeckList = new ArrayList<>(deckList);
        mDeck = new Deck(deck);
    }

    public FileWriter(ArrayList<Deck> deckList, Deck deck, String deckFile) {
        mDeckList = new ArrayList<>(deckList);
        mDeck = new Deck(deck);
        mDeckFile = new File(deckFile);
    }

    public FileWriter(ArrayList<Deck> deckList, String deckFile){
        mDeckList = new ArrayList<>(deckList);
        mDeckFile = new File(deckFile);
    }

    public FileWriter(ArrayList<Deck> deckList, String deckFile, String gradeFile) {
        mDeckFile = new File(deckFile);
        mGradeFile = new File(gradeFile);
        mDeckList = new ArrayList<>(deckList);
    }

    @Override
     public void run(){
        try{
             writeToStorage(mDeckList);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void writeToStorage(ArrayList<Deck> deckList) throws IOException {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        mDeckList = new ArrayList<>(deckList);
        fileOutputStream = new FileOutputStream(mDeckFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(deckList);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void initiateStorage() throws IOException {
        if (mDeckFile.createNewFile())
            Log.d(TAG, "Created deck file");
        else
            Log.d(TAG, "Deck file already exists");
    }

    public void initiateGradeStorage() throws IOException {
        if (mGradeFile.createNewFile())
            Log.d(TAG, "Created grade file");
        else
            Log.d(TAG, "Grade file already exists");

    }
}
