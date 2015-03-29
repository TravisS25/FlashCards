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
 * Created by Travis on 3/22/2015.
 */
public class DeckPositionReader implements IStorageReader {

    private final static String TAG = "DeckPositionReader";
    private File mDeckPositionFile;

    public DeckPositionReader(String fileName){ mDeckPositionFile = new File(fileName); }

    public HashMap<String, Integer> readFromStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        HashMap<String, Integer> deckPosition;

        fileInputStream = new FileInputStream(mDeckPositionFile);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        deckPosition = (HashMap<String, Integer>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return deckPosition;
    }

    public HashMap<String, Integer> call(){
        try{
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
        if (mDeckPositionFile.createNewFile())
            Log.d(TAG, "Created deck file");
        else
            Log.d(TAG, "Deck file already exists");
    }

}
