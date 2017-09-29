package com.example.pc.tablaperiodica;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.pc.tablaperiodica.data.QuestionsContract;
import com.example.pc.tablaperiodica.data.QuestionsData;

public class TableActivity extends AppCompatActivity {

    public static final String QUESTION_NUMBER_KEY = "mQuestionNumber";
    public static final String USER_ANSWERS_KEY = "mUserAnswers";

    Context mContext;

    private int mQuestionNumber;

    private GridView mTableView;
    private ElementsAdapter mAdapter;

    private int[] mCorrectAnswer;
    private int[] mUserAnswers;

    private QuestionsDBHelper mDbHelper;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.table_menu, menu);
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

//        mQuestionNumber = getIntent().getExtras().getInt(QUESTION_NUMBER_KEY, -1);
//        mUserAnswers = getIntent().getExtras().getIntArray(USER_ANSWERS_KEY);
        mQuestionNumber = 1;
        mUserAnswers = new int[0];

//        mCorrectAnswer = QuestionsData.getAnswer(mQuestionNumber);

        mContext = this;

        mTableView = (GridView) findViewById(R.id.tableGridView);

        mAdapter = new ElementsAdapter(mContext);

//        mTableView.setAdapter(mAdapter);

        mDbHelper = new QuestionsDBHelper(mContext);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case android.R.id.home:
//                new writeAnswersTask(mAdapter.getSelectedAnswersAsString()).execute((Void) null);
//                finish();
//                return true;
//            default:
//
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    @Override
    protected void onStop() {
        super.onStop();
//        new writeAnswersTask(mAdapter.getSelectedAnswersAsString()).execute((Void) null);
    }

    private class writeAnswersTask extends AsyncTask<Void, Void, Void>{

        private String answersToWrite;

        writeAnswersTask(String answers){
            answersToWrite = answers;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(QuestionsContract.AnswersEntry.COLUMN_USER_ANSWERS, answersToWrite);

            int status = QuestionsActivity.PENDING_QUESTION;
//            if(mAdapter.correctlyAnswered()){
//                status = QuestionsActivity.CORRECT_QUESTION;
//            } else if(mAdapter.selectedAnswers() > 0){
//                status = QuestionsActivity.WRONG_QUESTION;
//            }

            values.put(QuestionsContract.AnswersEntry.COLUMN_ANSWERED, status);

            String selection = QuestionsContract.AnswersEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(mQuestionNumber)};

            db.update(
                    QuestionsContract.AnswersEntry.QUESTION_TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                    );
            db.close();



            return null;
        }
    }
}
