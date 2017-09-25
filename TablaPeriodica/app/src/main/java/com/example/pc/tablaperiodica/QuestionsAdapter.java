package com.example.pc.tablaperiodica;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.tablaperiodica.data.QuestionsData;

/**
 * Created by pc on 24/09/2017.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsAdapterViewHolder> {

    private final QuestionsAdapterOnClickHandler mClickHandler;


    public interface QuestionsAdapterOnClickHandler {
        void onClick(int questionNumber);
    }

    public QuestionsAdapter(QuestionsAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }


    public class QuestionsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mQuestionTextView;
        public final TextView mQuestionNumber;
        public final View mQuestionSeparator;

        public QuestionsAdapterViewHolder(View view) {
            super(view);
            mQuestionTextView = (TextView) view.findViewById(R.id.tv_question);
            mQuestionSeparator = (View) view.findViewById(R.id.question_separator);
            mQuestionNumber = (TextView) view.findViewById(R.id.tv_question_number);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }


    @Override
    public QuestionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.question_list_item, parent, shouldAttachToParentImmediately);
        return new QuestionsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsAdapterViewHolder questionsAdapterViewHolder , int position) {

        String question = QuestionsData.getQuestion(position);
        questionsAdapterViewHolder.mQuestionTextView.setText(question);
        questionsAdapterViewHolder.mQuestionNumber.setText(getCharForNumber(position)+".");

        if(position == getItemCount()-1)
            questionsAdapterViewHolder.mQuestionSeparator.setVisibility(View.INVISIBLE);
        else
            questionsAdapterViewHolder.mQuestionSeparator.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return QuestionsData.QUESTION_COUNT;
    }

    private String getCharForNumber(int i) {
        return i >= 0 && i < 27 ? String.valueOf((char)(i + 'a')) : null;
    }
}
