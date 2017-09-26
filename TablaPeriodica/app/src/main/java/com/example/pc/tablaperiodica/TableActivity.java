package com.example.pc.tablaperiodica;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.pc.tablaperiodica.data.QuestionsData;

public class TableActivity extends AppCompatActivity {

    public static final String QUESTION_NUMBER_KEY = "mQuestionNumber";

    Context mContext;

    private int mQuestionNumber;

    private GridView mTableView;
    private ElementsAdapter mAdapter;

    private int[] mAnswer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.table_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        mQuestionNumber = getIntent().getExtras().getInt(QUESTION_NUMBER_KEY, -1);
        mAnswer = QuestionsData.getAnswer(mQuestionNumber);

        mContext = this;

        mTableView = (GridView) findViewById(R.id.tableGridView);

        mAdapter = new ElementsAdapter(this);
        mTableView.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.send_answers:
                mAdapter.showAnswers(mAnswer, mTableView);
                return true;
            case R.id.clear_answers:
                mAdapter.clearSelection(mTableView);
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }


    }
}
