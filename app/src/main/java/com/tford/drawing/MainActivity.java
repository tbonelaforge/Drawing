package com.tford.drawing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by tford on 3/22/17.
 */
public class MainActivity extends Activity {
    private MyView myView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        myView = (MyView) findViewById(R.id.my_view);
    }

    public void drawLine(View view) {
        System.out.println("Inside drawLine, got called!");
        EditText x1EditText = (EditText) findViewById(R.id.x_1_text);
        EditText x2EditText = (EditText) findViewById(R.id.x_2_text);
        EditText yEditText = (EditText) findViewById(R.id.y_text);
        try {
            int x1 = Integer.parseInt(x1EditText.getText().toString());
            int x2 = Integer.parseInt(x2EditText.getText().toString());
            int y = Integer.parseInt(yEditText.getText().toString());
            myView.drawHorizontalLine(x1, x2, y);
        } catch (NumberFormatException e) {
            System.out.println("There was an error parsing the input as numbers!");
        }
    }
}
