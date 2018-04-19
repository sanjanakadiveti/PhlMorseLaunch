package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import phlmorse.gatech.edu.phlmorse.R;
import phlmorse.gatech.edu.phlmorse.model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText unameField;
    private EditText emailfield;
    private EditText passwordfield;
    private EditText passwordRepeatField;
    private Button registerButton;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        unameField = findViewById(R.id.username);
        emailfield = findViewById(R.id.usernameField);
        passwordfield = findViewById(R.id.passwordField);
        passwordRepeatField = findViewById(R.id.passwordRepeatField);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = unameField.getText().toString();
                String email = emailfield.getText().toString();
                String password = passwordfield.getText().toString();
                String passwordCheck = passwordRepeatField.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordCheck)) {
                    Toast.makeText(getApplicationContext(), "Enter password repeat!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(passwordCheck)) {
                    dbRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "Username taken!", Toast.LENGTH_SHORT).show();
                            } else {
                                new User(username, email, password);
                                Intent intent = new Intent(RegisterActivity.this, TutorialActivity.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
