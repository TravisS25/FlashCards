package gamerzdisease.com.flashcards.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 4/1/2015.
 */
public class DeckWriter implements IStorageWriter {
    private final static String TAG = "StudyWriter";
    private ArrayList<Deck> mDeckList;
    private File mFile;

    public DeckWriter(ArrayList<Deck> deckList, String fileName){
        mDeckList = new ArrayList<>(deckList);
        mFile = new File(fileName);
    }

    @Override
    public void run(){
        try{
            writeToStorage();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void writeToStorage() throws IOException {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        fileOutputStream = new FileOutputStream(mFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(mDeckList);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }
}
