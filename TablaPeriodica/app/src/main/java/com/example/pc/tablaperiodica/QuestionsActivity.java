package com.example.pc.tablaperiodica;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pc.tablaperiodica.data.QuestionsContract;
import com.example.pc.tablaperiodica.data.QuestionsContract.AnswersEntry;
import com.example.pc.tablaperiodica.data.QuestionsData;
import com.example.pc.tablaperiodica.data.TableElements;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements QuestionsAdapter.QuestionsAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private QuestionsAdapter mQuestionsAdapter;
    private Context mContext;
    private QuestionsDBHelper mDbHelper;

    private List<Question> mQuestionsList;

    public static final int PENDING_QUESTION = 0;
    public static final int WRONG_QUESTION = -1;
    public static final int CORRECT_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        mRecyclerView = (RecyclerView) findViewById(R.id.questions_recyclerview);

        mContext = QuestionsActivity.this;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mQuestionsAdapter = new QuestionsAdapter(this);
        mRecyclerView.setAdapter(mQuestionsAdapter);

        mQuestionsList = new ArrayList<>();

        mDbHelper = new QuestionsDBHelper(mContext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.questions_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.reset_answers:
                new resetAnswers().execute((Void) null);
                mQuestionsAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class resetAnswers extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            int[] question_status = new int[QuestionsData.QUESTION_COUNT];
            mQuestionsList = new ArrayList<>();

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            for(int i=0; i< QuestionsData.QUESTION_COUNT; i++){
                ContentValues values = new ContentValues();
                values.put(AnswersEntry.COLUMN_ANSWERED, PENDING_QUESTION);
                values.put(AnswersEntry.COLUMN_USER_ANSWERS, "");

                String selection = QuestionsContract.AnswersEntry._ID + " = ?";
                String[] selectionArgs = {String.valueOf(i)};

                db.update(
                        QuestionsContract.AnswersEntry.QUESTION_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
            }

            Cursor cursor = db.query(
                    AnswersEntry.QUESTION_TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while(cursor.moveToNext()){
                int id = cursor.getInt(
                        cursor.getColumnIndex(AnswersEntry._ID)
                );

                int status = cursor.getInt(
                        cursor.getColumnIndex(AnswersEntry.COLUMN_ANSWERED)
                );

                String answers = cursor.getString(
                        cursor.getColumnIndex(AnswersEntry.COLUMN_USER_ANSWERS)
                );

                Question newQuestion = new Question(id, status, answers);

                mQuestionsList.add(newQuestion);
            }

            cursor.close();
            db.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mQuestionsAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onResume() {
        new ReadDatabase().execute((Void) null);
        mQuestionsAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onClick(int questionNumber) {

//        mQuestionsAdapter.notifyDataSetChanged();

        Context context = QuestionsActivity.this;
        Class targetClass = TableActivity.class;
        Intent intent = new Intent(context, targetClass);

        intent.putExtra(TableActivity.QUESTION_NUMBER_KEY, questionNumber);
        int[] answers = mQuestionsList.get(questionNumber).userAnswers;
        intent.putExtra(TableActivity.USER_ANSWERS_KEY, answers);


        startActivity(intent);

    }

    public class ReadDatabase extends AsyncTask<Void, Void, Void>{

        QuestionsDBHelper mDbHelper = new QuestionsDBHelper(mContext);

        int[] question_status = new int[QuestionsData.QUESTION_COUNT];

        SQLiteDatabase db;

        public ReadDatabase () {

        }


        @Override
        protected Void doInBackground(Void... params) {
            db = mDbHelper.getReadableDatabase();

            Cursor cursor = db.query(
                    AnswersEntry.QUESTION_TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                    );

            if(cursor.getCount() == 0){
                db = mDbHelper.getWritableDatabase();

                for(int i=0; i< QuestionsData.QUESTION_COUNT; i++){
                    ContentValues values = new ContentValues();
                    values.put(AnswersEntry._ID, i);
                    values.put(AnswersEntry.COLUMN_USER_ANSWERS, "");

                    long newRowID = db.insert(AnswersEntry.QUESTION_TABLE_NAME, null, values);
                }
                db.close();

                cursor = db.query(
                        AnswersEntry.QUESTION_TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            }

            mQuestionsList = new ArrayList<>();

            while(cursor.moveToNext()){
                int id = cursor.getInt(
                        cursor.getColumnIndex(AnswersEntry._ID)
                );

                int status = cursor.getInt(
                        cursor.getColumnIndex(AnswersEntry.COLUMN_ANSWERED)
                );

                String answers = cursor.getString(
                        cursor.getColumnIndex(AnswersEntry.COLUMN_USER_ANSWERS)
                );

                Question newQuestion = new Question(id, status, answers);

                mQuestionsList.add(newQuestion);
            }

            mQuestionsAdapter.setQuestionList(mQuestionsList);

            db.close();
            cursor.close();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class Question {
        int id;
        int status;
        int[] userAnswers;

        public Question(int id, int status, int[] userAnswers){
            this.id = id;
            this.status = status;
            this.userAnswers = userAnswers;
        }

        public Question(int id, int status, String userAnswers){
            this.id = id;
            this.status = status;
            if(userAnswers != null && userAnswers.length() > 0) {
                userAnswers = userAnswers.substring(0,userAnswers.length()-1);
                String[] answersStrings = userAnswers.split(",");
                this.userAnswers = new int[answersStrings.length];

                for (int i = 0; i < answersStrings.length; i++) {
                    this.userAnswers[i] = Integer.parseInt(answersStrings[i]);
                }
            } else {
                this.userAnswers = new int[0];
            }
        }
    }
}
