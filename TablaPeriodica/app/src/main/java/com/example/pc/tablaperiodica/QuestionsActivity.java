package com.example.pc.tablaperiodica;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.pc.tablaperiodica.data.QuestionsContract;
import com.example.pc.tablaperiodica.data.QuestionsContract.AnswersEntry;
import com.example.pc.tablaperiodica.data.QuestionsData;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements QuestionsAdapter.QuestionsAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private QuestionsAdapter mQuestionsAdapter;
    private Context mContext;
    private QuestionsDBHelper mDbHelper;

    private GridView mTableView;
    private ElementsAdapter mElementsAdapter;
    private ConstraintLayout mTableLayout;
    private TextView mQuestionTitle;

    private List<Question> mQuestionsList;

    private int mQuestionNumber;

    private int[] mCorrectAnswer;
    private int[] mUserAnswers;

    public static final int PENDING_QUESTION = 0;
    public static final int WRONG_QUESTION = -1;
    public static final int CORRECT_QUESTION = 1;

    private boolean mTableVisible = false;
    private boolean permissionToAnimate = false; //Variable extra para solucionar un bug desconocido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_questions);

        mRecyclerView = (RecyclerView) findViewById(R.id.questions_recyclerview);

        mContext = QuestionsActivity.this;

        mTableLayout = (ConstraintLayout) findViewById(R.id.tableLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mQuestionsAdapter = new QuestionsAdapter(mContext, this);
        mRecyclerView.setAdapter(mQuestionsAdapter);

        mQuestionsList = new ArrayList<>();

        mDbHelper = new QuestionsDBHelper(mContext);

        mQuestionNumber = 1;
        mUserAnswers = new int[0];

        mContext = this;

        new ReadDatabase().execute((Void) null);

        mTableView = (GridView) findViewById(R.id.tableGridView);

        mElementsAdapter = new ElementsAdapter(mContext);

        mQuestionTitle  = (TextView) findViewById(R.id.question_title);

        mTableView.setAdapter(mElementsAdapter);

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int questionNumber) {
        showTable(questionNumber);

    }

    @Override
    public void onBackPressed() {
        if(mTableVisible){
            showQuestions();
        }
    }

    void showTable(int questionNumber){
        permissionToAnimate = true;

        mQuestionNumber = questionNumber;

        mQuestionTitle.setText(QuestionsData.getQuestion(mQuestionNumber));
        mUserAnswers = mQuestionsList.get(questionNumber).userAnswers;
        mCorrectAnswer = QuestionsData.getAnswer(questionNumber);

        mElementsAdapter.notifyTableVisibility(false);

        mElementsAdapter.setNewData(mUserAnswers, mCorrectAnswer, mTableView);
        float width = mRecyclerView.getWidth();
        width *= 1.5;
        mRecyclerView.animate().setDuration(600)
                .setStartDelay(100).translationX(-width).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(permissionToAnimate) {
                    permissionToAnimate = false;

                    getSupportActionBar().hide();

                    mTableLayout.setVisibility(View.VISIBLE);
                    mTableLayout.setAlpha(0f);
                    mTableLayout.animate().setDuration(600).alpha(1f).start();
                    mRecyclerView.setVisibility(View.GONE);
                    mRecyclerView.setAlpha(1f);

                    mTableVisible = true;
                    mElementsAdapter.notifyTableVisibility(mTableVisible);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    void showQuestions(){
        permissionToAnimate = true;

        new writeAnswersTask(mElementsAdapter.getSelectedAnswersAsString()).execute((Void) null);

        mQuestionsList.get(mQuestionNumber).userAnswers = mElementsAdapter.getSelectedAnswersAsArray();

        int status = PENDING_QUESTION;
        if(mElementsAdapter.correctlyAnswered()){
            status = CORRECT_QUESTION;
        } else if(mElementsAdapter.selectedAnswers() > 0){
            status = WRONG_QUESTION;
        }
        mQuestionsList.get(mQuestionNumber).status = status;
        mQuestionsAdapter.updateData(mQuestionsList);

        mElementsAdapter.notifyTableVisibility(mTableVisible);
        mTableLayout.animate().setDuration(600).alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(permissionToAnimate) {
                    permissionToAnimate = false;
                    getSupportActionBar().show();

                    mTableVisible = false;

                    mTableLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    mRecyclerView.animate().setDuration(600).translationX(0).start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
            mQuestionsAdapter.updateData(mQuestionsList);
            mQuestionsAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private class writeAnswersTask extends AsyncTask<Void, Void, Void>{

        private String answersToWrite;

        writeAnswersTask(String answers){
            answersToWrite = answers;
            Log.d("prueba", answersToWrite);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(QuestionsContract.AnswersEntry.COLUMN_USER_ANSWERS, answersToWrite);

            int status = QuestionsActivity.PENDING_QUESTION;
            if(mElementsAdapter.correctlyAnswered()){
                status = QuestionsActivity.CORRECT_QUESTION;
            } else if(mElementsAdapter.selectedAnswers() > 0){
                status = QuestionsActivity.WRONG_QUESTION;
            }

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

        @Override
        protected void onPostExecute(Void aVoid) {
//            new ReadDatabase().execute((Void) null);
        }
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
            mQuestionsAdapter.updateData(mQuestionsList);
            super.onPostExecute(aVoid);
        }
    }

    public class Question {
        int id;
        int status;
        int[] userAnswers;

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
