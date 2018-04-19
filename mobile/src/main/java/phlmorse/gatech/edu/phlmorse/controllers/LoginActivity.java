package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.User;

/**
 * Created by sanjanakadiveti on 3/19/18.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText unamefield;
    private EditText passwordfield;
    private Button login;
    private Button register;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unamefield = findViewById(R.id.usernameField);
        passwordfield = findViewById(R.id.passwordField);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = unamefield.getText().toString();
                final String password = passwordfield.getText().toString();

                if (TextUtils.isEmpty(uname)) {
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference dbRefUsers = dbRef.child("users");
                dbRefUsers.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(uname)) {
                            if (dataSnapshot.child("password").getValue().equals(password)) {
                                Intent intent;
                                if ((long)dataSnapshot.child("completed").getValue() == 0) {
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
        });
    }
}