package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.User;

/**
 * Created by sanjanakadiveti on 4/22/18.
 */

public class LessonListActivity extends AppCompatActivity{
    ListView lessonsView;
    private final ArrayList<String> lessons = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference lessonRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);
        lessonsView = findViewById(R.id.lessonList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference shelterRefPre = database.getReference();
        lessonRef = shelterRefPre.child("users").child(getIntent().getStringExtra("Username"));
        adapter = new ArrayAdapter<>(LessonListActivity.this,
                android.R.layout.simple_list_item_1, lessons);
        lessonsView.setAdapter(adapter);
        showLessons();
        lessonsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) lessonsView.getItemAtPosition(i);
                Intent intent = new Intent(LessonListActivity.this, LearningActivity.class);
                intent.putExtra("Username", getIntent().getStringExtra("Username"));
                intent.putExtra("Quiz", "relearn");
                intent.putExtra("SelectedWord", selected);
                startActivity(intent);
                finish();
            }
        });
    }
    private void showLessons() {
        lessonRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("completed")) {
                    long numQuizzes = (long) dataSnapshot.getValue();
                    for (int i = 0; i < (int)numQuizzes; i++) {
                        lessons.add(User.getQuiz(i).getWord());
                        adapter.notifyDataSetChanged();
                    }
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
    }
}
