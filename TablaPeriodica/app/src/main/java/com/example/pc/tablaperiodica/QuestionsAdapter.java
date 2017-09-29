package com.example.pc.tablaperiodica;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
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
    private int[] statusIconId;

    private Context mContext;


    public interface QuestionsAdapterOnClickHandler {
        void onClick(int questionNumber);
    }

    public QuestionsAdapter(Context context, QuestionsAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
        mContext = context;
        statusIconId = new int[QuestionsData.QUESTION_COUNT];
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

    public void updateData(List<Question> newQuestions){
        questionList = newQuestions;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(QuestionsAdapterViewHolder questionsAdapterViewHolder , int position) {

        String question = QuestionsData.getQuestion(position);
        questionsAdapterViewHolder.mQuestionTextView.setText(question);
        questionsAdapterViewHolder.mQuestionNumber.setText(getCharForNumber(position)+".");

        if(questionList != null) {

            int red = mContext.getResources().getColor(R.color.red);
            int black = mContext.getResources().getColor(R.color.black);
            int green = mContext.getResources().getColor(R.color.green);

                switch (questionList.get(position).status) {
                    case QuestionsActivity.WRONG_QUESTION:
                        if(statusIconId[position] != QuestionsActivity.WRONG_QUESTION){
                            animateImageAndSetIcon(
                                    questionsAdapterViewHolder.mStatusIcon,
                                    R.mipmap.ic_cancel_black_24dp,
                                    red);
                        } else {
                            questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_cancel_black_24dp);
                            questionsAdapterViewHolder.mStatusIcon.setColorFilter(
                                    mContext.getResources().getColor(R.color.red));
                        }
                        statusIconId[position] = QuestionsActivity.WRONG_QUESTION;
                        break;
                    case QuestionsActivity.PENDING_QUESTION:
                        if(statusIconId[position] != QuestionsActivity.PENDING_QUESTION){

                            if(statusIconId[position] == QuestionsActivity.WRONG_QUESTION){
                                questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_cancel_black_24dp);
                                questionsAdapterViewHolder.mStatusIcon.setColorFilter(
                                        ContextCompat.getColor(mContext, R.color.red));
                            } else if(statusIconId[position] == QuestionsActivity.CORRECT_QUESTION){
                                questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_check_circle_black_24dp);
                                questionsAdapterViewHolder.mStatusIcon.setColorFilter(
                                        ContextCompat.getColor(mContext, R.color.green));
                            }

                            animateImageAndSetIcon(
                                    questionsAdapterViewHolder.mStatusIcon,
                                    R.mipmap.ic_watch_later_black_24dp,
                                    black);
                        } else {
                            questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_watch_later_black_24dp);
                            questionsAdapterViewHolder.mStatusIcon.setColorFilter(
                                    mContext.getResources().getColor(R.color.black));
                        }
                        statusIconId[position] = QuestionsActivity.PENDING_QUESTION;
                        break;
                    case QuestionsActivity.CORRECT_QUESTION:
                        if(statusIconId[position] != QuestionsActivity.CORRECT_QUESTION){
                            if(statusIconId[position] == QuestionsActivity.WRONG_QUESTION){
                                questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_cancel_black_24dp);
                                questionsAdapterViewHolder.mStatusIcon.setColorFilter(
                                        ContextCompat.getColor(mContext, R.color.red));
                            }
                            animateImageAndSetIcon(
                                    questionsAdapterViewHolder.mStatusIcon,
                                    R.mipmap.ic_check_circle_black_24dp,
                                    green);
                        } else {
                            questionsAdapterViewHolder.mStatusIcon.setImageResource(R.mipmap.ic_check_circle_black_24dp);
                            questionsAdapterViewHolder.mStatusIcon.setColorFilter(
                                    ContextCompat.getColor(mContext, R.color.green));
                        }
                        statusIconId[position] = QuestionsActivity.CORRECT_QUESTION;
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

    void animateImageAndSetIcon(final ImageView viewToAnimate, final int iconId , final int newColor){
        int duration = 100;
        viewToAnimate.animate().setDuration(duration).scaleX(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewToAnimate.setImageResource(iconId);
                viewToAnimate.setColorFilter(newColor);
                viewToAnimate.animate().setDuration(100).scaleX(1f).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
