package phlmorse.gatech.edu.phlmorse.controllers;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.Quiz;
import phlmorse.gatech.edu.phlmorse.model.User;

/**
 * Created by sanjanakadiveti on 3/19/18.
 */

public class ApplicationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    Button nextQuizBtn;
    Button refreshMemBtn;
    Button relearn;
    Button allResults;
    String username;
    long quizNumber;
    String toLearn;
    String preTaken;

    private static GoogleApiClient googleApiClient;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");
    // variables for communication
    private static Node mNode; // the connected device to send the message to
    private boolean mResolvingError = false;
    public static String SERVICE_CALLED_WEAR = "WearListClicked";
    public static String TAG = "WearListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        username = getIntent().getExtras().get("Username").toString();
        nextQuizBtn = findViewById(R.id.NextQuizButton);
        refreshMemBtn = findViewById(R.id.MemRefreshButton);
        relearn = findViewById(R.id.quizlistbtn);
        allResults = findViewById(R.id.viewresultsbtn);
        getToLearn();

        nextQuizBtn.setOnClickListener((view)-> {
            if (toLearn == null) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ApplicationActivity.this)
                        .setTitle("No More New Lessons!")
                        .setMessage("You have completed all your lessons. Go to re-learn a word to learn a specific word again.");
                AlertDialog alertDialog = adb.create();
                //alertDialog.setTitle("Alert Dialog");
                //alertDialog.setMessage("Welcome to dear user.");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                });
                adb.show();
            } else {
                Intent intent;
                if (preTaken.equals("no")) {
                    intent = new Intent(ApplicationActivity.this, QuizActivity.class);
                    intent.putExtra("PrePost", "pre");
                } else {
                    intent = new Intent(ApplicationActivity.this, LearningActivity.class);
                }
                intent.putExtra("Username", username);
                intent.putExtra("Quiz", "next");
                startActivity(intent);
                finish();
            }
        });

        refreshMemBtn.setOnClickListener((view) -> {
            Intent intent = new Intent(ApplicationActivity.this, QuizActivity.class);
            intent.putExtra("Username", username);
            intent.putExtra("Quiz", "refresher");
            intent.putExtra("PrePost", "none");
            startActivity(intent);
            finish();
        });
        relearn.setOnClickListener((view) -> {
            Intent intent = new Intent(ApplicationActivity.this, LessonListActivity.class);
            intent.putExtra("Username", username);
            startActivity(intent);
            finish();
        });
        allResults.setOnClickListener((view -> {
            Intent intent = new Intent(ApplicationActivity.this, ResultsListActivity.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        }));
    }

    private void sendMessage(final String message) {
        if(googleApiClient != null &&
                googleApiClient.isConnected() &&
                mNode != null) {
            Log.d(TAG,"Message is going to be sent to watch");
            Wearable.MessageApi.sendMessage(googleApiClient, mNode.getId(),"/"+message, message.getBytes())
                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                            if(sendMessageResult.getStatus().isSuccess()) {
                                Log.e(TAG,"Message Succesfully sent to watch=>"+message + mNode.getId());
                            } else {
                                Log.e(TAG,"Message FAILED TO BE SENT to watch=>"+message);
                            }
                        }
                    });
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sendMessage("starting");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    private void getToLearn() {
        dbRef.child(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("completed")) {
                    quizNumber = (long) dataSnapshot.getValue();
                    if (quizNumber == 8) {
                        toLearn = null;
                    } else {
                        toLearn = User.getQuiz((int) quizNumber).getWord();
                        checkPreTaken();
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
    private void checkPreTaken() {
        dbRef.child(username).child("quizzes").child(toLearn).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("pre-taken")) {
                    preTaken = dataSnapshot.getValue().toString();
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
