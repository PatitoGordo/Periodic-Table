package com.example.pc.tablaperiodica;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pc.tablaperiodica.data.TableElements;

/**
 * Created by pc on 23/09/2017.
 */

public class ElementsAdapter extends BaseAdapter{

    Context mContext;

    public ElementsAdapter(Context context){
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        int elementIndex = tableIndex[position];
        View elementView;
        if(convertView == null){
            elementView = TableElements.getElementView(mContext, elementIndex);
        } else {
            elementView = convertView;
            TableElements.SetElemenNumberAndSymbol(elementIndex, elementView);
        }
        return elementView;
    }

    public static int clickedElement(int position){
        return tableIndex[position];
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
            0,  0,  0,  57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
            0,  0,  0,  89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100,101,102,103

    };
}
