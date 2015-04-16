package gamerzdisease.com.flashcards.filesystem;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by Travis on 3/4/2015.
 */
public interface IStorageReader extends Callable<Object> {
    Object readFromStorage() throws IOException, ClassNotFoundException;
    void initiateStorage() throws IOException;
}
