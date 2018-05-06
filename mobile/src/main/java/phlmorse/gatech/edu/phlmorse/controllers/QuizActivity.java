package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.Quiz;
import phlmorse.gatech.edu.phlmorse.model.User;

/**
 * Created by sanjanakadiveti on 3/19/18.
 */

public class QuizActivity extends AppCompatActivity {
    String username;
    long quizNumber;
    Quiz q;
    String quizType;
    String toTest;
    LinearLayout layout;
    Button submitBtn;
    ImageButton morse;
    private long morseCodeInputTime;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        layout = findViewById(R.id.quizQuestions);
        submitBtn = findViewById(R.id.SubmitButton);
        username = getIntent().getExtras().get("Username").toString();
        quizType = getIntent().getStringExtra("Quiz");
        dbRef.child(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("completed")) {
                    quizNumber = (long)dataSnapshot.getValue();
                    toTest = "";
                    if (quizType.equals("refresher")) {
                        for (int i = 0; i < (int)quizNumber; i++) {
                            q = User.getQuiz(i);
                            toTest += q.getWord();
                        }
                    } else {
                        q = User.getQuiz((int)quizNumber);
                        toTest = q.getWord();
                    }
                    populateLetters(toTest);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        morse = (ImageButton) findViewById(R.id.morse);
        morse.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(getCurrentFocus() instanceof TextView) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        morseCodeInputTime = System.currentTimeMillis();
                    } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if(System.currentTimeMillis() - morseCodeInputTime < 300) {
                            // Dot
                            ((TextView) getCurrentFocus()).append(".");
                        } else {
                            // Dash
                            ((TextView) getCurrentFocus()).append("-");
                        }
                    }
                }
                return false;
            }
        });
        submitBtn.setOnClickListener((view) -> {
            Intent intent;
            if (getIntent().getStringExtra("PrePost").equals("pre")) {
                FirebaseDatabase.getInstance()
                        .getReference().child("users").child(username)
                        .child("quizzes").child(toTest).child("pre-taken").setValue("yes");
                intent = new Intent(QuizActivity.this, LearningActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("Quiz", "next");
                startActivity(intent);
            } else {
                if (!(quizType.equals("refresher"))) {
                    dbRef.child(username).child("completed").setValue((int) quizNumber + 1);
                }
                intent = new Intent(QuizActivity.this, ResultsActivity.class);
                intent.putExtra("Username", username);
                for (int i = 0; i < toTest.length(); i++) {
                    LinearLayout questionHorizontal = (LinearLayout) layout.getChildAt(i);
                    EditText input = (EditText) questionHorizontal.getChildAt(1);
                    String userAnswer = input.getText().toString();
                    intent.putExtra(String.valueOf(toTest.charAt(i)), userAnswer);
                }
                intent.putExtra("TestedString", toTest);
                startActivity(intent);
                finish();
            }
        });
    }
    private void populateLetters(String word) {

        for (int i = 0; i < word.length(); i++) {

            // HorizontalLayout to store question and input
            LinearLayout questionHorizontalLayout = new LinearLayout(this);
            questionHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            questionHorizontalLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            // TextView for Character
            TextView question = new TextView(this);
            question.setText(String.valueOf(word.charAt(i)));


            // EditText for input
            EditText questionInput = new EditText(this);
            questionInput.setShowSoftInputOnFocus(false);
            //questionInput.setKeyListener(DigitsKeyListener.getInstance(".-"));

            // Append to horizontal layout
            questionHorizontalLayout.addView(question);
            questionHorizontalLayout.addView(questionInput);

            // Append horizontal layout to vertical layout
            layout.addView(questionHorizontalLayout);
        }
    }
}
