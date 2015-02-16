package gamerzdisease.com.flashcards.deck;

import android.app.Application;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Travis on 2/15/2015.
 */
public class DeckHolder extends Application {

    private ArrayList<Deck> deckList;
    private File file;
    private final static String TAG = "Deckholder";

    @Override
    public void onCreate(){
        super.onCreate();
        initiateDeckFile();
        try {
            getDecksFromFile();
        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }

    }

    public void saveDeckName(Deck deckName){

    }

//==================================================================================================

    private void initiateDeckFile() {
        String filePath = this.getFilesDir() + "/" + Consts.FILENAME;
        file = new File(filePath);
        try {
            if (this.file.createNewFile())
                Log.d(TAG, "Created file");
            else
                Log.d(TAG, "File already exists");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void getDecksFromFile() throws IOException, ClassNotFoundException{
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;

        this.deckList = new ArrayList<>();
        fileInputStream = new FileInputStream(this.file);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        this.deckList = (ArrayList<Deck>)objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();

    }
}
