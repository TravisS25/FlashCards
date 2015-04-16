package gamerzdisease.com.flashcards.filesystem;

import java.io.IOException;

/**
 * Created by Travis on 3/3/2015.
 */
public interface IStorageWriter extends Runnable{
    void writeToStorage() throws IOException;
}
