package phlmorse.gatech.edu.phlmorse.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sanjanakadiveti on 3/19/18.
 */

public class User implements Parcelable {
    private static final ArrayList<Quiz> ALLQUIZZES = getAllQuizzes();
    private static Map<String, User> users = new HashMap<>();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference dbRef = database.getReference();
    private String username;
    private String email;
    private String password;
    private int completed;

    public User(String u, String uuid) {
        username = u;
        password = uuid;
        completed = 0;

        dbRef.child("users").child(username).child("password").setValue(uuid);
        dbRef.child("users").child(username).child("completed").setValue(completed);
        dbRef.child("uuidToName").child(uuid).setValue(u);
        for (Quiz q : ALLQUIZZES) {
            dbRef.child("users").child(username).child("quizzes").child(q.getWord()).child("pre-taken").setValue("no");
        }
    }
    public static Quiz getQuiz(int quizNumber) {
        return ALLQUIZZES.get(quizNumber);
    }
    private User(Parcel in) {
        email = in.readString();
    }
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPassword() {
        return password;
    }
    public Quiz getNextQuiz() {
        if (completed == ALLQUIZZES.size()) {
            return null;
        }
        return ALLQUIZZES.get(completed);
    }
    public boolean isNewUser() {
        return (completed == 0);
    }
    public static ArrayList<Quiz> getAllQuizzes() {
        ArrayList<Quiz> allQuizzes = new ArrayList<Quiz>();
        allQuizzes.add(new Quiz("The"));
        allQuizzes.add(new Quiz("Quick"));
        allQuizzes.add(new Quiz("Brown"));
        allQuizzes.add(new Quiz("Fox"));
        allQuizzes.add(new Quiz("Jumps"));
        allQuizzes.add(new Quiz("Over"));
        allQuizzes.add(new Quiz("Lazy"));
        allQuizzes.add(new Quiz("Dog"));
        return allQuizzes;
    }
}
