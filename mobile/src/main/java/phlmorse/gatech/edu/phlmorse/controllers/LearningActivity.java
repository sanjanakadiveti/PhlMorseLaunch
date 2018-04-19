package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.User;

/**
 * Created by sanjanakadiveti on 4/3/18.
 */

public class LearningActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    Button cancel;
    Button done;
    String qtype;
    long quizNumber;
    static String toLearn;
    static int timesLeft;
    String username;
    static Node mNode;
    static int currIndex;
    private static GoogleApiClient googleApiClient;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        timesLeft = 20;
        cancel = findViewById(R.id.cancelBtn);
        done = findViewById(R.id.doneBtn);
        qtype = getIntent().getStringExtra("Quiz");
        username = getIntent().getStringExtra("Username");

        dbRef.child(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("completed")) {
                    quizNumber = (long)dataSnapshot.getValue();
                    toLearn = "";
                    if (qtype.equals("refresher")) {
                        for (int i = 0; i < (int)quizNumber; i++) {
                            toLearn += User.getQuiz(i).getWord();
                            currIndex = 0;
                        }
                    } else {
                        toLearn = User.getQuiz((int)quizNumber).getWord();
                        currIndex = 0;
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("cancel");
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timesLeft >= 15) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(LearningActivity.this)
                            .setTitle("Learning Hazard")
                            .setMessage("At this point in time, if you quit, you may not learn as much.");
                    AlertDialog alertDialog = adb.create();
                    alertDialog.setTitle("Alert Dialog");
                    alertDialog.setMessage("Welcome to dear user.");
                    adb.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                            sendMessage("cancel");
                            Intent intent = new Intent(LearningActivity.this, QuizActivity.class);
                            intent.putExtra("Username", getIntent().getStringExtra("Username"));
                            intent.putExtra("Quiz", qtype);
                            intent.putExtra("PrePost", "post");
                            startActivity(intent);
                            finish();
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.cancel();
                        }
                    });
                    adb.show();
                } else {
                    sendMessage("cancel");
                    Intent intent = new Intent(LearningActivity.this, QuizActivity.class);
                    intent.putExtra("Username", getIntent().getStringExtra("Username"));
                    intent.putExtra("Quiz", qtype);
                    intent.putExtra("PrePost", "post");
                    startActivity(intent);
                    finish();
                }
            }
        });
        initGoogleApiClient();
    }

    public void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
        learn();
    }
    public static void learn() {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult connectedNodes) {
                for (Node connectedNode : connectedNodes.getNodes()) {
                    mNode = connectedNode;
                    String learningNow;
                    if (currIndex < toLearn.length()) {
                        learningNow = toLearn.substring(currIndex, currIndex + 1);
                        sendMessage(learningNow);
                        if (currIndex == toLearn.length() - 1) {
                            if (timesLeft > 0) {
                                currIndex = 0;
                                timesLeft--;
                            } else {
                                currIndex++;
                            }
                        } else {
                            currIndex++;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        sendMessage("cancel");
        finish();
    }

    private static void sendMessage(String message) {
        if(googleApiClient != null &&
                googleApiClient.isConnected() &&
                mNode != null) {
            Log.d("cut2", message);
            ApplicationActivity.speakString(message);
            Wearable.MessageApi.sendMessage(googleApiClient, mNode.getId(),message, message.getBytes())
                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                            if(sendMessageResult.getStatus().isSuccess()) {
                                Log.d(message, message.getBytes().toString()+"sent");
                            } else {
                                Log.d(message, "Notsent");
                            }
                        }
                    });
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
