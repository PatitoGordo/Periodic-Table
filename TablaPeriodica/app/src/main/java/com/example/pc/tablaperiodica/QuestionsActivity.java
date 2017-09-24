package com.example.pc.tablaperiodica;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class QuestionsActivity extends AppCompatActivity implements QuestionsAdapter.QuestionsAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private QuestionsAdapter mQuestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        mRecyclerView = (RecyclerView) findViewById(R.id.questions_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mQuestionsAdapter = new QuestionsAdapter(this);
        mRecyclerView.setAdapter(mQuestionsAdapter);
    }


    @Override
    public void onClick(int questionNumber) {
        Context context = QuestionsActivity.this;
        Toast.makeText(context, "You pressed question "+questionNumber, Toast.LENGTH_SHORT).show();
    }
}
