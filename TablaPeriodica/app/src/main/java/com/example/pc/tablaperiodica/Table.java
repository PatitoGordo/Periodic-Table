package com.example.pc.tablaperiodica;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.StringBuilderPrinter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.example.pc.tablaperiodica.data.TableElements;

import org.w3c.dom.Text;

public class Table extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        mContext = Table.this;

        GridView table = (GridView) findViewById(R.id.tableGridView);
        table.setAdapter(new ElementsAdapter(this));

        table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int clickedElementNumber = ElementsAdapter.clickedElement(position);
                Toast.makeText(mContext, "You clicked on " + TableElements.getElementName(clickedElementNumber), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
