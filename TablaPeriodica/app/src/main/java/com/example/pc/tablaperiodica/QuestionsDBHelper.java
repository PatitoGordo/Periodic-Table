package com.example.pc.tablaperiodica;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pc.tablaperiodica.data.QuestionsContract;
import com.example.pc.tablaperiodica.data.QuestionsContract.AnswersEntry;
import com.example.pc.tablaperiodica.data.QuestionsData;

/**
 * Created by Hector on 26/09/2017.
 */

public class QuestionsDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "questions_db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QuestionsContract.AnswersEntry.QUESTION_TABLE_NAME + "(" +
                    AnswersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AnswersEntry.COLUMN_ANSWERED + " INTEGER NOT NULL DEFAULT 0, " +
                    AnswersEntry.COLUMN_USER_ANSWERS + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AnswersEntry.QUESTION_TABLE_NAME;

    public QuestionsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
//        db = this.getWritableDatabase();
//
//        for(int i=0; i< QuestionsData.QUESTION_COUNT; i++){
//            ContentValues values = new ContentValues();
//            values.put(AnswersEntry._ID, i);
//            values.put(AnswersEntry.COLUMN_USER_ANSWERS, "");
//
//            long newRowID = db.insert(AnswersEntry.QUESTION_TABLE_NAME, null, values);
//        }
//        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
