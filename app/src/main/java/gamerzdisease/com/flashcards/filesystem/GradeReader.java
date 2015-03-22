package gamerzdisease.com.flashcards.filesystem;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 3/20/2015.
 */
public class GradeReader implements IStorageReader {
    private final static String TAG = "GradeReader";
    private File mDeckFile;

    public GradeReader(String deckFile){
        mDeckFile = new File(deckFile);
    }

    public HashMap<String, ArrayList<Integer>> readFromStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        HashMap<String, ArrayList<Integer>> gradeList;

        fileInputStream = new FileInputStream(mDeckFile);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        gradeList = (HashMap<String, ArrayList<Integer>>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return gradeList;
    }

    public HashMap<String, ArrayList<Integer>> call(){
        try{
            initiateStorage();
            return readFromStorage();
        }
        catch (EOFException ex){
            Log.d(TAG, "EOFException reached");
            return new HashMap<>();
        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
            ex.getMessage();
            Log.d(TAG, "Reached exception");
            return null;
        }
    }

    public void initiateStorage() throws IOException{
        if (mDeckFile.createNewFile())
            Log.d(TAG, "Created deck file");
        else
            Log.d(TAG, "Deck file already exists");
    }

}
