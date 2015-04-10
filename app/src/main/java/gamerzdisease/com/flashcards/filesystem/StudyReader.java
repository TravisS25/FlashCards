package gamerzdisease.com.flashcards.filesystem;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Created by Travis on 3/30/2015.
 */
public class StudyReader implements IStorageReader {

    private final static String TAG = "DeckPositionReader";
    private File mStudyFile;

    public StudyReader(String fileName){ mStudyFile = new File(fileName); }

    public HashMap<String, Boolean> readFromStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        HashMap<String, Boolean> deckPosition;

        fileInputStream = new FileInputStream(mStudyFile);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        deckPosition = (HashMap<String, Boolean>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return deckPosition;
    }

    public HashMap<String, Boolean> call(){
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
        if (mStudyFile.createNewFile())
            Log.d(TAG, "Created deck file");
        else
            Log.d(TAG, "Deck file already exists");
    }
}
