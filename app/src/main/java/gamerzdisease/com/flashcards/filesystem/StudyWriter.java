package gamerzdisease.com.flashcards.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 3/30/2015.
 */
public class StudyWriter implements Runnable {

    private final static String TAG = "StudyWriter";
    private HashMap<String, Boolean> mIsFirstStudy;
    private File mFile;

    public StudyWriter(HashMap<String, Boolean> isFirstStudy, String filename){
        mIsFirstStudy = new HashMap<>(isFirstStudy);
        mFile = new File(filename);
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

    protected void writeToStorage() throws IOException {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        fileOutputStream = new FileOutputStream(mFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(mIsFirstStudy);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }
}
