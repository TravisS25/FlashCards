package gamerzdisease.com.flashcards.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 3/22/2015.
 */
public class DeckPositionWriter implements IStorageWriter {

    private File mDeckPositionFile;
    private HashMap<String, Integer> mDeckPosition;

    public DeckPositionWriter(HashMap<String, Integer> deckposition, String fileName){
        mDeckPositionFile = new File(fileName);
        mDeckPosition = new HashMap<>(deckposition);
    }

    public void writeToStorage(Object object)throws IOException {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        mDeckPosition = new HashMap<>((HashMap<String, Integer>)object);
        fileOutputStream = new FileOutputStream(mDeckPositionFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(mDeckPosition);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void run(){
        try{
            writeToStorage(mDeckPosition);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }


}
