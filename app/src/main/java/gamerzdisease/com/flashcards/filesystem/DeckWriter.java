package gamerzdisease.com.flashcards.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gamerzdisease.com.flashcards.deck.Deck;


/**
 * Created by Travis on 3/2/2015.
 */
public class DeckWriter implements IStorageWriter {

    private final static String TAG = "FileWriter";
    private ArrayList<Deck> mDeckList;
    private Deck mDeck;
    private File mDeckFile;

    public DeckWriter(ArrayList<Deck> deckList) {
        mDeckList = new ArrayList<>(deckList);
    }

    public DeckWriter(ArrayList<Deck> deckList, Deck deck) {
        mDeckList = new ArrayList<>(deckList);
        mDeck = new Deck(deck);
    }

    public DeckWriter(ArrayList<Deck> deckList, Deck deck, String deckFile) {
        mDeckList = new ArrayList<>(deckList);
        mDeck = new Deck(deck);
        mDeckFile = new File(deckFile);
    }

    public DeckWriter(ArrayList<Deck> deckList, String deckFile){
        mDeckList = new ArrayList<>(deckList);
        mDeckFile = new File(deckFile);
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

    public void writeToStorage(Object deckList) throws IOException {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        mDeckList = new ArrayList<>((ArrayList<Deck>)deckList);
        fileOutputStream = new FileOutputStream(mDeckFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(deckList);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }


}
