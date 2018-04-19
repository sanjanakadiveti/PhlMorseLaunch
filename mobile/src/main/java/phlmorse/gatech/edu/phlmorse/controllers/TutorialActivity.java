package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import phlmorse.gatech.edu.phlmorse.R;

/**
 * Created by sanjanakadiveti on 3/20/18.
 */

public class TutorialActivity extends AppCompatActivity {
    String username;
    Button next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        username = getIntent().getStringExtra("Username");
        next = findViewById(R.id.nextbutton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorialActivity.this, DotTutorialActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
    }
}
