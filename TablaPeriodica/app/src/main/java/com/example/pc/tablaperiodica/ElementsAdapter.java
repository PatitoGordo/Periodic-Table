package com.example.pc.tablaperiodica;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.tablaperiodica.data.TableElements;

import static com.example.pc.tablaperiodica.data.TableElements.tableIndex;

/**
 * Created by pc on 23/09/2017.
 */

public class ElementsAdapter extends BaseAdapter{

    private static Context mContext;

    private static boolean[] selectedElements;

    public ElementsAdapter(Context context){
        mContext = context;
        selectedElements = new boolean[TableElements.ELEMENT_COUNT];
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
        int elementIndex = tableIndex[position];
        if(elementView == null){
            elementView = TableElements.getElementView(mContext, elementIndex);
        }


        TableElements.SetElementNumberAndSymbol(elementIndex, elementView);

        elementView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        clickedElement(v);
                        break;
                }
                return false;
            }
        });

        return elementView;
    }

    public void clearSelection(GridView grid){

        int brightColor = mContext.getResources().getColor(R.color.elementColor);
        int darkColor = mContext.getResources().getColor(R.color.elementColorDark);

        for (int i=0; i<getCount(); i++){
            if(getElementIndexFromPosition(i) > 0) {
                View element = grid.getChildAt(i);
                element.findViewById(R.id.element_status_icon).setVisibility(View.GONE);
                final TextView numberTextView = (TextView) element.findViewById(R.id.tv_atomic_number);
                int elementIndex = Integer.parseInt(numberTextView.getText().toString());

                if (elementIsSelected(elementIndex)) {

                    selectedElements[elementIndex] = false;

                    int animDuration = 70;
                    float scale = 1.2f;
                    animateElement(element, brightColor, darkColor);
                }
            }

        }
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

    public void clickedElement(final View v){

        final TextView numberTextView = (TextView) v.findViewById(R.id.tv_atomic_number);
        final int elementIndex = Integer.parseInt(numberTextView.getText().toString());

        selectedElements[elementIndex] = !selectedElements[elementIndex];

        int brightColor = mContext.getResources().getColor(R.color.elementColor);
        int darkColor = mContext.getResources().getColor(R.color.elementColorDark);

        if(elementIsSelected(elementIndex)){
            animateElement(v, darkColor, brightColor);
        }
        else{
            animateElement(v, brightColor, darkColor);
        }

    }



    public void animateElement(final View v, final int backgroundColor, final int textColor){
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

    public void changeElementColor(View elementView, int backgroundColor, int textColor){

        animateElement(elementView, backgroundColor, textColor);


    }

    public void clickedElement(int position){ //Se manda a llamar cuando se clickea un elemento en position x

        int elementIndex = getElementIndexFromPosition(position);

        selectedElements[elementIndex] = !elementIsSelected(elementIndex);

        notifyDataSetChanged();
    }

    public void showAnswers(int[] answers, GridView table){
        for(int elementIndex : answers){

            int greenColor = mContext.getResources().getColor(R.color.green);
            int blackColor = mContext.getResources().getColor(R.color.black);

            changeElementColor(
                    table.getChildAt(getElementPositionFromIndex(elementIndex))
                    ,greenColor, blackColor);
        }
    }
}
