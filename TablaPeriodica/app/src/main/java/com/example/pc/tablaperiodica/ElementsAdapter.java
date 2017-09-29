package com.example.pc.tablaperiodica;

import android.animation.Animator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.pc.tablaperiodica.data.ElementsData;

import static com.example.pc.tablaperiodica.data.ElementsData.tableIndex;

/**
 * Created by pc on 23/09/2017.
 */

public class ElementsAdapter extends BaseAdapter{

    private static Context mContext;

    private static int[] mSelectedAnswers;
    private static int[] mCorrectAnswers;
    private static GridView mGridView;
    private static int mSelectedCount;

    private static boolean[] selectedElements;

    private static int mCorrectAnswerCount;

    public ElementsAdapter(Context context){
        mContext = context;
    }

    public int[] getSelectedAnswersAsArray(){
        int count = 0;
        for(int i = 0; i< ElementsData.ELEMENT_COUNT; i++){
            if(selectedElements[i]){
                count++;
            }
        }
        int[] selectedAnswers = new int[count];
        count = 0;
        for(int i = 0; i< ElementsData.ELEMENT_COUNT; i++){
            if(selectedElements[i]){
                selectedAnswers[count] = i;
                count++;
            }
        }
        return selectedAnswers;
    }

    public String getSelectedAnswersAsString(){
        String s = "";
        for(int i = 0; i< ElementsData.ELEMENT_COUNT; i++){
            if(selectedElements[i]){
                s = s.concat(i+",");
            }
        }
        return s;
    }

    @Override
    public int getCount() {
        return tableIndex.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View elementView, ViewGroup parent) {
        View newView;
        int elementIndex = tableIndex[position];
        if(elementView == null){
            newView = ElementsData.getElementView(mContext, elementIndex);
        } else {
            newView = elementView;
        }
        ElementsData.SetElementNumberAndSymbol(elementIndex, newView);

        if(selectedElements != null) {

            if (!elementIsSelected(elementIndex)) {//TODO arreglar, selectedElements[] es null

                newView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                clickedElement(v);
                                break;
                        }
                        return false;
                    }
                });

            } else {

                int backgroundColor = mContext.getResources().getColor(R.color.red);
                int textColor = mContext.getResources().getColor(R.color.black);

                if (answerIsCorrect(elementIndex)) {
                    backgroundColor = mContext.getResources().getColor(R.color.green);
                }

                changeElementColor(newView, backgroundColor, textColor);

            }
        }

        return newView;
    }

    public int getElementIndexFromPosition(int position){
        return tableIndex[position];
    }

    public int getElementPositionFromIndex(int index){
        for(int i=0; i<tableIndex.length; i++){
            if(tableIndex[i] == index)
                return i;
        }
        return -1;
    }

    public static boolean elementIsSelected(int elementIndex){
        return selectedElements[elementIndex];
    }

    public void setNewData( int[] selectedAnswers, int[] correctAnswers, GridView gridView ){
        mSelectedAnswers = selectedAnswers;
        mCorrectAnswers = correctAnswers;
        mGridView = gridView;
        mCorrectAnswerCount = 0;
        mSelectedCount = 0;
        selectedElements = new boolean[ElementsData.ELEMENT_COUNT];

        for(int selectedElementIndex : mSelectedAnswers) {
            selectedElements[selectedElementIndex] = true;
            if(answerIsCorrect(selectedElementIndex)){
                mCorrectAnswerCount++;
            }
        }

        notifyDataSetChanged();

    }

    public void clickedElement(final View v){
        if(mCorrectAnswerCount == mCorrectAnswers.length)
            return;

        mSelectedCount++;
        final TextView numberTextView = (TextView) v.findViewById(R.id.tv_atomic_number);
        final int elementIndex = Integer.parseInt(numberTextView.getText().toString());

        selectedElements[elementIndex] = !selectedElements[elementIndex];

        int black = mContext.getResources().getColor(R.color.black);
        int green = mContext.getResources().getColor(R.color.green);
        int red = mContext.getResources().getColor(R.color.red);


        if(answerIsCorrect(elementIndex)){

            mCorrectAnswerCount++;

            if(mCorrectAnswerCount == mCorrectAnswers.length){

                final View backgroundView = v.findViewById(R.id.background_view);
                final TextView symbolTextview = (TextView) v.findViewById(R.id.tv_element_symbol);

                numberTextView.setTextColor(black);
                symbolTextview.setTextColor(black);
                backgroundView.setBackgroundColor(green);

                playAnimation();
            } else {
                changeElementColor(v, green, black);
            }
        }
        else{
            changeElementColor(v, red, black);
        }

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    public boolean correctlyAnswered(){
        return (mCorrectAnswerCount == mCorrectAnswers.length);
    }

    public int selectedAnswers(){
        return mSelectedCount;
    }

    void playAnimation(){
        for(int i=0; i<ElementsData.tableIndex.length; i++){
            final View v = mGridView.getChildAt(i);
            if(getElementIndexFromPosition(i) > 0) {
                v.animate().setDuration(100).scaleX(0.5f).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.animate().setDuration(100).scaleX(1f).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }
    }

    boolean answerIsCorrect(int elementIndex){
        for(int answer : mCorrectAnswers){
            if(answer == elementIndex)
                return true;
        }
        return false;
    }

    public void animateView(final View v){

        final int animDuration = 100;

        v.animate().setDuration(animDuration).scaleX(0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.animate().setDuration(animDuration).scaleX(1f).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    public void changeElementColor(final View v, final int backgroundColor, final int textColor){
        final View backgroundView = v.findViewById(R.id.background_view);
        final TextView numberTextView = (TextView) v.findViewById(R.id.tv_atomic_number);
        final TextView symbolTextview = (TextView) v.findViewById(R.id.tv_element_symbol);


        final int animDuration = 100;

        v.animate().setDuration(animDuration).scaleX(0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if(textColor != 0) {
                            symbolTextview.setTextColor(textColor);
                            numberTextView.setTextColor(textColor);
                        }
                        if(backgroundColor != 0) {
                            backgroundView.setBackgroundColor(backgroundColor);
                        }
                        v.animate().setDuration(animDuration).scaleX(1f).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}
