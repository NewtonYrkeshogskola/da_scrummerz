package se.newton.scrummerz;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;

import org.w3c.dom.Comment;

import se.newton.scrummerz.model.Student;

public class grades_activity extends AppCompatActivity {

    String userId, myClass, myGrades;

    DatabaseReference dbRef;
    DatabaseReference gradesRef;
    private RecyclerView allCourses;
    SharedPreferences studentInfo;

    private RecyclerView allGrades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_activity);
        dbRef = FirebaseDatabase.getInstance().getReference();
        getUid();
        getMyGrades(userId);

        allGrades = (RecyclerView) findViewById(R.id.coursesRecyclerView);
        allGrades.setHasFixedSize(false);
        allGrades.setLayoutManager(new LinearLayoutManager(this));
    }


    public void getUid() {
        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException ignored) {
            // ...
        }
    }

    public void getMyGrades(String user) {
        DatabaseReference localRef = dbRef.child("users").child("students").child(user).child("grades").child("final");

        localRef.addChildEventListener(new ChildEventListener() {

            public static final String TAG = "hejhej";

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Comment comment = dataSnapshot.getValue(Comment.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Comment newComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "postComments:onCancelled", databaseError.toException());
                //Context mContext = ;
                //Toast.makeText(mContext, "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
