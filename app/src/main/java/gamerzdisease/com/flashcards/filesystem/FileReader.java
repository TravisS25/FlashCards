package gamerzdisease.com.flashcards.filesystem;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;

/**
 * Created by Travis on 3/4/2015.
 */
public class FileReader implements IStorageReader {

    private final static String TAG = "FileReader";
    private ArrayList<Deck> mDeckList;
    private File mDeckFile, mGradeFile;

    public FileReader(ArrayList<Deck> deckList) {
        mDeckList = new ArrayList<>(deckList);
    }

    public FileReader(ArrayList<Deck> deckList, String deckFile) {
        mDeckList = new ArrayList<>(deckList);
        mDeckFile = new File(deckFile);
    }

    public FileReader(String deckfile, String gradeFile) {
        mDeckList = new ArrayList<>();
        mDeckFile = new File(deckfile);
        mGradeFile = new File(gradeFile);
    }

    public FileReader(ArrayList<Deck> deckList, String deckfile, String gradeFile){
        mDeckList = new ArrayList<>(deckList);
        mDeckFile = new File(deckfile);
        mGradeFile = new File(gradeFile);
    }

    public ArrayList<Deck> readFromStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        ArrayList<Deck> deckList;

        fileInputStream = new FileInputStream(mDeckFile);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        deckList = (ArrayList<Deck>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return deckList;
    }

    public void initiateDeckStorage() throws IOException{
        if (mDeckFile.createNewFile())
            Log.d(TAG, "Created deck file");
        else
            Log.d(TAG, "Deck file already exists");
    }

    public void initiateGradeStorage() throws IOException{
        if (mGradeFile.createNewFile())
            Log.d(TAG, "Created grade file");
        else
            Log.d(TAG, "Grade file already exists");
    }

    public ArrayList<Deck> call(){
        try{
            initiateDeckStorage();
            initiateGradeStorage();
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
}
