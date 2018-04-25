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
 * Created by sanjanakadiveti on 3/20/18.
 */

public class DotTutorialActivity extends AppCompatActivity {
    String username;
    TextView tv;
    ImageButton buttonCodeInput;
    private long morseCodeInputTime;
    Button next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot);
        tv = findViewById(R.id.dotDashText);
        next = findViewById(R.id.nextDotbutton);
        username = getIntent().getStringExtra("Username");

        buttonCodeInput = (ImageButton) findViewById(R.id.button3);
        buttonCodeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (tv).append(".");
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv.getText().toString().contains("...")) {
                    Intent intent = new Intent(DotTutorialActivity.this, DashTutorialActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter at least one dot and dash!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
