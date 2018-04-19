package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.Quiz;

/**
 * Created by sanjanakadiveti on 3/27/18.
 */

public class ResultsActivity extends AppCompatActivity {
    private String tested;
    private TextView scoreView;
    private TextView correctView;
    private TextView incorrectView;
    private String correctLetters;
    private String incorrectLetters;
    Button submitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        tested = getIntent().getStringExtra("TestedString");
        scoreView = findViewById(R.id.displayScore);
        correctView = findViewById(R.id.displayCorrect);
        incorrectView = findViewById(R.id.displayIncorrect);
        submitBtn = findViewById(R.id.doneButton);
        correctLetters = "";
        incorrectLetters = "";
        for (int i = 0; i < tested.length(); i ++) {
            if (Quiz.isCorrect(String.valueOf(tested.charAt(i)), getIntent().getStringExtra(String.valueOf(tested.charAt(i))))) {
                correctLetters += String.valueOf(tested.charAt(i)).toUpperCase();
            } else {
                incorrectLetters += String.valueOf(tested.charAt(i)).toUpperCase();
            }
        }
        double score = ((double)correctLetters.length() / tested.length()) * 100;
        scoreView.setText(String.valueOf(score) + "%");
        correctView.setText(correctLetters);
        incorrectView.setText(incorrectLetters);
        writeToDatabase();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void writeToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child("users");
        String username = getIntent().getStringExtra("Username");
        dbRef.child(username).child("postQuizzes").child(tested).child("correct").setValue(correctLetters);
        dbRef.child(username).child("postQuizzes").child(tested).child("incorrect").setValue(incorrectLetters);
    }
}
