package gamerzdisease.com.flashcards;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Travis on 1/31/2015.
 */
public class MenuHandler {
    private Activity mActivity;

    public MenuHandler(Activity activity) {
        mActivity = activity;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = mActivity.getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                return true;
        }
        return true;
    }
}

