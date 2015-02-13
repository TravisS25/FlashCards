package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import gamerzdisease.com.flashcards.deck.Consts;
import gamerzdisease.com.flashcards.deck.Deck;
import gamerzdisease.com.flashcards.deck.DeckAdapter;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private AdapterView.OnItemClickListener onItemClickListener;
    private File file;
    private ArrayList<Deck> deckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //createobject();
        initiateDeckFile();

        try {
            getDecksFromFile();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.deck_listview);
        BaseAdapter deckAdapter = new DeckAdapter(this.deckList);
        listView.setAdapter(deckAdapter);
        initiateListener();
        listView.setOnItemClickListener(this.onItemClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newDeck(View V) {
        initiateNewDeckIntent();
    }

    //==============================================================================================

    private void getDecksFromFile() throws IOException, ClassNotFoundException{
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;

        this.deckList = new ArrayList<>();
        fileInputStream = new FileInputStream(this.file);
        objectInputStream = new ObjectInputStream(fileInputStream);
        this.deckList = (ArrayList<Deck>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();

    }

    private void initiateListener(){
       onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), OptionsActivity.class);
                String name = deckList.get(position).getName();
                intent.putExtra(Consts.DECKNAME, name);
                startActivity(intent);
            }
        };
    }

    private void initiateNewDeckIntent() {
        Intent intent = new Intent(this, NewDeckActivity.class);
        startActivity(intent);
    }

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

    private void createobject(){
        this.deckList = new ArrayList<>();
    }


}
