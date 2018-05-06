package phlmorse.gatech.edu.phlmorse.controllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import phlmorse.gatech.edu.phlmorse.R;

/**
 * Created by sanjanakadiveti on 4/26/18.
 */

public class ResultsListActivity extends AppCompatActivity {
    ListView resultsList;
    private DatabaseReference resultsRef;

    private final ArrayList<String> results = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);
        resultsList = findViewById(R.id.allresults);
        resultsRef = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("Username"))
                .child("quizzes");
    }
    private void getResults() {
        resultsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //String s = dataSnapshot.
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
