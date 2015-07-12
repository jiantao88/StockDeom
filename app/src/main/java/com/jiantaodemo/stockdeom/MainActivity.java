package com.jiantaodemo.stockdeom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private android.widget.RelativeLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.linear = (RelativeLayout) findViewById(R.id.linear);
        List<Integer> lists = new ArrayList<Integer>();// 线性图 范围10-100
        for (int i = 0; i < 50; i++) {
            if (i < 8 || i == 28 || i == 12 || i == 18 || i == 20 || i == 30
                    || i == 34) {
                lists.add(0);
            } else {
                lists.add(getRandom(0, 500));
            }
        }
        linear.addView(new LinearGraphView(this, lists));
    }

    public int getRandom(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
