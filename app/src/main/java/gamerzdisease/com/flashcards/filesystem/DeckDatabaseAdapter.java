package gamerzdisease.com.flashcards.filesystem;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gamerzdisease.com.flashcards.adapters.DeckAdapter;
import gamerzdisease.com.flashcards.deck.Deck;


/**
 * Created by Travis on 4/1/2015.
 */
public class DeckDatabaseAdapter {

    DeckHelper mHelper;
    private final static String TAG = "DeckDatabaseAdapter";

    public DeckDatabaseAdapter(Context context) {
        mHelper = new DeckHelper(context);
    }

    public long tableInsert(String deckTable, String hackColumn, ContentValues contentValues) {
        Table table = new Table(deckTable);
        return table.insert(hackColumn, contentValues);
    }

    public Cursor tableQuery(String deckTable, String[] columns, String selection, String[] selectionArgs,
                             String groupBy, String having, String orderBy, String limit) {
        Table table = new Table(deckTable);
        return table.select(columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public long tableReplace(String deckTable, String hackColumn, ContentValues contentValues) {
        Table table = new Table(deckTable);
        return table.replace(hackColumn, contentValues);
    }

    public int tableRemove(String deckTable, String whereClause, String[] whereArgs) {
        Table table = new Table(deckTable);
        return table.delete(whereClause, whereArgs);
    }

    public Cursor tableRawQuery(String sql, String[] selectionArgs){
        Table table = new Table();
        return table.rawQuery(sql, selectionArgs);
    }

    public static class DeckHelper extends SQLiteOpenHelper {
        private final static String TAG = "DeckHelper";

        public final static String DATABASE_NAME = "FlashCards.db";
        public final static String GRADE_TABLE = "Grades";
        public final static String STUDY_INFO_TABLE = "StudyInfo";
        public final static String UID_COLUMN = "_id";
        public final static String DECK_NAME_COLUMN = "Deck_Name";
        public final static String STUDY_POSITION_COLUMN = "Study_Position";
        public final static String GRADE_COLUMN = "Grade";
        public final static String GRADE_POSITION_COLUMN = "Grade_position";
        public final static String REMAINING_QUESTIONS_COLUMN = "Remaining_Questions";
        public final static String STUDY_MODE_COLUMN = "Study_Mode";
        public final static String DATE_COLUMN = "Date";
        private final static String DROP_STUDY_INFO_TABLE = "DROP_TABLE IF EXISTS " + STUDY_INFO_TABLE;
        private final static String DROP_GRADE_TABLE = "DROP_TABLE IF EXISTS " + GRADE_TABLE;
        private final static int DATABASE_VERSION = 1;

        Context mContext;

        private final static String CREATE_GRADE_TABLE = "CREATE TABLE "+ GRADE_TABLE + " ( " +
                "" + UID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "" + DECK_NAME_COLUMN + " TEXT NOT NULL, " +
                "" + GRADE_COLUMN + " REAL NOT NULL, " +
                "" + DATE_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        private final static String CREATE_STUDY_INFO_TABLE = "CREATE TABLE " + STUDY_INFO_TABLE + " ( " +
                "" + UID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "" + DECK_NAME_COLUMN + " TEXT NOT NULL UNIQUE, " +
                "" + STUDY_POSITION_COLUMN + " INTEGER DEFAULT Null, " +
                "" + REMAINING_QUESTIONS_COLUMN + " TEXT DEFAULT Null, " +
                "" + GRADE_POSITION_COLUMN + " INTEGER NOT NULL, " +
                "" + STUDY_MODE_COLUMN + " INTEGER NOT NULL " +
                ");";


        private DeckHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d(TAG, "Deckhelper constructor");
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "Sqlhelper OnCreate");
            try {
                db.execSQL(CREATE_STUDY_INFO_TABLE);
                db.execSQL(CREATE_GRADE_TABLE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_STUDY_INFO_TABLE);
                db.execSQL(DROP_GRADE_TABLE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            onCreate(db);
        }
    }

    //Table within the database
    private class Table {

        private String mDatabaseTable;

        private Table(){}
        private Table(String databaseTable) {
            mDatabaseTable = databaseTable;
        }

        public long insert(final String hackColumn, final ContentValues contentValues) {
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return db.insert(mDatabaseTable, hackColumn, contentValues);
                }
            };
            Future<Long> future = executor.submit(callable);

            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            return -1;
        }

        private Cursor select(final String[] columns, final String selection, final String[] selectionArgs,
                              final String groupBy, final String having, final String orderBy, final String limit) {
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Callable<Cursor> callable = new Callable<Cursor>() {
                @Override
                public Cursor call() throws Exception {
                    return db.query(mDatabaseTable, columns, selection, selectionArgs, null, null, null);
                }
            };
            Future<Cursor> future = executor.submit(callable);

            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        private long replace(final String hackColumn, final ContentValues contentValues) {
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return db.replace(mDatabaseTable, hackColumn, contentValues);
                }
            };
            Future<Long> future = executor.submit(callable);

            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            return -1;
        }

        private int delete(final String whereClause, final String[] whereArgs){
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return db.delete(mDatabaseTable, whereClause, whereArgs);
                }
            };
            Future<Integer> future = executor.submit(callable);

            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            return -1;
        }

        private Cursor rawQuery(final String sql, final String[] selectionArgs){
            Log.d(TAG, "Table called");
            final SQLiteDatabase db = mHelper.getWritableDatabase();

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Callable<Cursor> callable = new Callable<Cursor>() {
                @Override
                public Cursor call() throws Exception {
                    return db.rawQuery(sql, selectionArgs);
                }
            };
            Future<Cursor> future = executor.submit(callable);

            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            return null;

        }
    }
}






