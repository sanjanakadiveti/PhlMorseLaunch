package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import phlmorse.gatech.edu.phlmorse.R;

/**
 * Created by sanjanakadiveti on 3/27/18.
 */

public class DashTutorialActivity extends AppCompatActivity {
    String username;
    TextView tv;
    ImageButton buttonCodeInput;
    private long morseCodeInputTime;
    Button next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        tv = findViewById(R.id.dotDashText);
        next = findViewById(R.id.nextDotbutton);
        username = getIntent().getStringExtra("Username");
        buttonCodeInput = (ImageButton) findViewById(R.id.button3);
        buttonCodeInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    morseCodeInputTime = System.currentTimeMillis();
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(System.currentTimeMillis() - morseCodeInputTime < 300) {
                        // Dot
                        (tv).append("");
                    } else {
                        // Dash
                        (tv).append("-");
                    }
                }

                return false;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv.getText().toString().contains("---")) {
                    Intent intent = new Intent(DashTutorialActivity.this, TutorialTwoActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter at least three dashes!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
