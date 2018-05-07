package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import phlmorse.gatech.edu.phlmorse.R;

/**
 * Created by sanjanakadiveti on 5/6/18.
 */

public class TutorialTwoActivity extends AppCompatActivity {
    Button next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutotial2);
        next = findViewById(R.id.tt2next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorialTwoActivity.this, TutorialThreeActivity.class);
                intent.putExtra("Username", getIntent().getStringExtra("Username"));
                startActivity(intent);
            }
        });
    }
}
