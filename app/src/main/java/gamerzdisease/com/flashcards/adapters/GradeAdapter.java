package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import gamerzdisease.com.flashcards.R;
import gamerzdisease.com.flashcards.filesystem.DeckDatabaseAdapter;

/**
 * Created by Travis on 3/1/2015.
 */
public class GradeAdapter extends CursorAdapter {

    private final static String TAG = "GradeAdapter";

    public GradeAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grade_listview_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String gradeColumn = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;
        String dateColumn = DeckDatabaseAdapter.DeckHelper.DATE_COLUMN;
        TextView gradeTextView = (TextView)view.findViewById(R.id.grade_text);
        TextView dateTextView = (TextView)view.findViewById(R.id.date_text);
        double gradeText = cursor.getDouble(cursor.getColumnIndex(gradeColumn));
        String dateText = cursor.getString(cursor.getColumnIndex(dateColumn));
        gradeTextView.setText(String.format("%.1f", gradeText) + "%");
        dateTextView.setText(dateText);
    }

}
