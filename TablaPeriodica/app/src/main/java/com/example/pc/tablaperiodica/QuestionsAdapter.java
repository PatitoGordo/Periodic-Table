package com.example.pc.tablaperiodica;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.tablaperiodica.data.QuestionsData;
import com.example.pc.tablaperiodica.QuestionsActivity.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 24/09/2017.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsAdapterViewHolder> {

    private final QuestionsAdapterOnClickHandler mClickHandler;

    private List<Question> questionList;


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
        public final ImageView mStatusIcon;

        public QuestionsAdapterViewHolder(View view) {
            super(view);
            mQuestionTextView = (TextView) view.findViewById(R.id.tv_question);
            mQuestionSeparator = (View) view.findViewById(R.id.question_separator);
            mQuestionNumber = (TextView) view.findViewById(R.id.tv_question_number);
            mStatusIcon = (ImageView) view.findViewById(R.id.question_status_icon);

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

        if(questionList != null) {

                switch (questionList.get(position).status) {
                    case QuestionsActivity.WRONG_QUESTION:
                        questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_cancel_black_24dp);
                        break;
                    case QuestionsActivity.PENDING_QUESTION:
                        questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_watch_later_black_24dp);
                        ScaleAnimation anim = new ScaleAnimation(
                                1f, 0.6f, // Start and end values for the X axis scaling
                                1f, 0.6f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        anim.setDuration(500);
                        anim.setRepeatCount(Animation.INFINITE);
                        anim.setRepeatMode(Animation.REVERSE);
                        questionsAdapterViewHolder.mStatusIcon.startAnimation(anim);
                        break;
                    case QuestionsActivity.CORRECT_QUESTION:
                        questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_check_circle_black_24dp);
                        break;
                    default:
                        break;
                }

        }


        if(position == getItemCount()-1)
            questionsAdapterViewHolder.mQuestionSeparator.setVisibility(View.INVISIBLE);
        else
            questionsAdapterViewHolder.mQuestionSeparator.setVisibility(View.VISIBLE);

    }

    public void setQuestionList (List<Question> questionList){
        this.questionList = questionList;
    }

    @Override
    public int getItemCount() {
        return QuestionsData.QUESTION_COUNT;
    }

    private String getCharForNumber(int i) {
        return i >= 0 && i < 27 ? String.valueOf((char)(i + 'a')) : null;
    }
}
