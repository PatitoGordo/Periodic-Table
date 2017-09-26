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

        for (int i=0; i<getCount(); i++){
            if(getElementIndexFromPosition(i) > 0) {
                View element = grid.getChildAt(i);
                final TextView numberTextView = (TextView) element.findViewById(R.id.tv_atomic_number);
                int elementIndex = Integer.parseInt(numberTextView.getText().toString());

                if (elementIsSelected(elementIndex)) {

                    selectedElements[elementIndex] = false;

                    int animDuration = 70;
                    float scale = 1.2f;
                    animateElement(element, animDuration, scale, false);
                }
            }

        }
    }

    public int getElementIndexFromPosition(int position){
        return tableIndex[position];
    }

    public static boolean elementIsSelected(int elementIndex){
        return selectedElements[elementIndex];
    }

    public void clickedElement(final View v){

        final TextView numberTextView = (TextView) v.findViewById(R.id.tv_atomic_number);
        final int elementIndex = Integer.parseInt(numberTextView.getText().toString());

        selectedElements[elementIndex] = !selectedElements[elementIndex];

        int animDuration = 70;
        float scale = 1.2f;

        animateElement(v, animDuration, scale, elementIsSelected(elementIndex));
    }



    public void animateElement(final View v, final int animDuration, final float scale, final boolean select){
        final View backgroundView = v.findViewById(R.id.background_view);
        final TextView numberTextView = (TextView) v.findViewById(R.id.tv_atomic_number);
        final TextView symbolTextview = (TextView) v.findViewById(R.id.tv_element_symbol);

        final int brightColor = mContext.getResources().getColor(R.color.elementColor);
        final int darkColor = mContext.getResources().getColor(R.color.elementColorDark);

        v.animate().setDuration(animDuration).scaleY(scale)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if(select){
                            symbolTextview.setTextColor(brightColor);
                            numberTextView.setTextColor(brightColor);
                            backgroundView.setBackgroundColor(darkColor);
                        }
                        else{
                            symbolTextview.setTextColor(darkColor);
                            numberTextView.setTextColor(darkColor);
                            backgroundView.setBackgroundColor(brightColor);
                        }

                        v.animate().setDuration(animDuration).scaleY(1f).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    public void clickedElement(int position){ //Se manda a llamar cuando se clickea un elemento en position x

        int elementIndex = getElementIndexFromPosition(position);

        selectedElements[elementIndex] = !elementIsSelected(elementIndex);

        notifyDataSetChanged();
    }

    public static final int[] tableIndex = {
            1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  2,
            3,  4,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  5,  6,  7,  8,  9,  10,
            11, 12, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
            37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54,
            55, 56, 57, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
            87, 88, 89, 104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,

            0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
            0,  0,  0,  0,  90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100,101,102,103

    };
}
