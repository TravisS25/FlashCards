package gamerzdisease.com.flashcards.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gamerzdisease.com.flashcards.deck.Deck;

/**
 * Created by Travis on 3/20/2015.
 */
public class GradeWriter implements IStorageWriter {

    private final static String TAG = "GradeWriter";
    private HashMap<String, ArrayList<Double>> mGradeList;
    private File mGradeFile;

    public GradeWriter(HashMap<String, ArrayList<Double>> grades, String gradeFile){
        mGradeFile = new File(gradeFile);
        mGradeList = new HashMap<>(grades);
    }

    @Override
    public void run(){
        try{
            writeToStorage(mGradeList);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void writeToStorage(Object grade) throws IOException {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        mGradeList = new HashMap<>((HashMap<String, ArrayList<Double>>)grade);
        fileOutputStream = new FileOutputStream(mGradeFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(mGradeList);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }
}
