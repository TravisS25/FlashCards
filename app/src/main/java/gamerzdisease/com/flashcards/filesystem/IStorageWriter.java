package gamerzdisease.com.flashcards.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckHolder;

/**
 * Created by Travis on 3/3/2015.
 */
public interface IStorageWriter extends Runnable{
    void writeToStorage(Object object) throws IOException;
}
