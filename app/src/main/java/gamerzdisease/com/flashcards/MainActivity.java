package gamerzdisease.com.flashcards;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private final static String FILENAME = "decks";
    private final static String TAG = "MainActivity";
    private ArrayList<Deck> decks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initiateDeckFile(this, FILENAME);
        addDecksToTable();
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
        initiateIntent();
    }

    //==============================================================================================

    private List<Deck> getDecksFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;

        fileInputStream = openFileInput(FILENAME);
        objectInputStream = new ObjectInputStream(fileInputStream);
        decks = (ArrayList<Deck>) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        return decks;
    }


    private void addDecksToTable() {
        TableLayout ll = (TableLayout) findViewById(R.id.main_table);

        try {
            if (getDecksFromFile() == null)
                return;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        for (Deck deck : decks) {
            String name = deck.getName();
            Log.d(TAG, "Name of deck: " + name);
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView deckView = new TextView(this);
            deckView.setText(deck.getName());
            row.addView(deckView);
            ll.addView(row);
        }

    }

    private void initiateIntent() {
        Intent intent = new Intent(this, NewDeckActivity.class);
        startActivity(intent);
    }

    private void initiateDeckFile(Context context, String filename) {
        File file = context.getFileStreamPath(FILENAME);
        if (file == null || !file.exists()) {
            file = new File(getFilesDir(), FILENAME);

        }

    }
}
