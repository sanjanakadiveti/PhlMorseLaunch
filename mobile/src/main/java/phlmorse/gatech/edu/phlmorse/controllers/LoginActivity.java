package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;

import phlmorse.gatech.edu.phlmorse.R;

/**
 * Created by sanjanakadiveti on 3/19/18.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText unamefield;
    String username;
    private ImageButton login;
    private Button register;
    String tempP;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FileInputStream fileInputStream = openFileInput("UUID");
            int c;
            tempP = "";
            while ((c = fileInputStream.read()) != -1) {
                tempP = tempP + Character.toString((char) c);
            }
            fileInputStream.close();
            DatabaseReference dbRefUsers = dbRef.child("uuidToName");
            dbRefUsers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getKey().equals(tempP)) {
                        username = dataSnapshot.getValue().toString();
                        Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
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

        } catch (Exception e) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
            /*setContentView(R.layout.activity_login);
            unamefield = findViewById(R.id.usernameField);
            //passwordfield = findViewById(R.id.passwordField);
            login = findViewById(R.id.loginButton);
            register = findViewById(R.id.registerButton);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uname = unamefield.getText().toString();
                    if (TextUtils.isEmpty(uname)) {
                        Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        FileInputStream fileInputStream = openFileInput("UUID");
                        int c;
                        tempP = "";
                        while ((c = fileInputStream.read()) != -1) {
                            tempP = tempP + Character.toString((char) c);
                        }

                        fileInputStream.close();
                    } catch (Exception e) {

                    }
                    DatabaseReference dbRefUsers = dbRef.child("users");
                    dbRefUsers.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().equals(uname)) {
                                if (dataSnapshot.child("password").getValue().equals(tempP)) {
                                    Intent intent;
                                    if ((long) dataSnapshot.child("completed").getValue() == 0) {
                                        intent = new Intent(LoginActivity.this, TutorialActivity.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, ApplicationActivity.class);
                                    }
                                    intent.putExtra("Username", uname);
                                    startActivity(intent);
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
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("Username", unamefield.getText().toString());
                    startActivity(intent);
                }
            });*/
        }
    }
}