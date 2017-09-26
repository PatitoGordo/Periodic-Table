package com.example.pc.tablaperiodica;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.StringBuilderPrinter;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

public class TableActivity extends AppCompatActivity {

    public static final String QUESTION_NUMBER_KEY = "questionNumber";

    Context mContext;

    private int questionNumber;

    private GridView mTableView;
    private ElementsAdapter mAdapter;

    private GestureDetector gestureDetector;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.table_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        questionNumber = getIntent().getExtras().getInt(QUESTION_NUMBER_KEY, -1);

        mContext = this;

        mTableView = (GridView) findViewById(R.id.tableGridView);

        mAdapter = new ElementsAdapter(this);
        mTableView.setAdapter(mAdapter);
//        mTableView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.setVisibility(View.INVISIBLE);
//                return false;
//            }
//        });

//        mTableView.setOnItemClickListener(new AdapterView.OnTouchListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                int clickedElementNumber = mAdapter.getElementIndexFromPosition(position);
//                mAdapter.clickedElement(position);
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.send_answers:
                //TODO Enviar respuestas
                return true;
            case R.id.clear_answers:
                mAdapter.clearSelection(mTableView);
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }


    }
}
