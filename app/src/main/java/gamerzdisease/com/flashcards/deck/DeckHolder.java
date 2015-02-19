package gamerzdisease.com.flashcards.deck;

import android.app.Application;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Travis on 2/15/2015.
 */
public class DeckHolder extends Application {

    private ArrayList<Deck> deckList;
    private File file;
    private Deck deck;
    private int position;
    private final static String TAG = "Deckholder";

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate called");
        initiateMainObjects();
        initiateDeckFile();
        try {
            getDecksFromFile();
        }
        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }

    }

    public void saveDeckName(String deckName){
        this.deck = new Deck();
        this.deck.setName(deckName);
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }

    public ArrayList<Deck> getDeckList(){
        return this.deckList;
    }

    public void writeDeckToFile() throws IOException{
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        this.deckList.add(this.deck);
        fileOutputStream = new FileOutputStream(this.file);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this.deckList);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void getDecksFromFile() throws IOException, ClassNotFoundException{
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;

        fileInputStream = new FileInputStream(this.file);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        this.deckList = (ArrayList<Deck>)objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();

    }

    public void addCardToDeck(String question, String answer){
        this.deck.setCard(question, answer);
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

    private void initiateMainObjects(){
        this.deckList = new ArrayList<>();
    }

}
