package se.newton.scrummerz;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class assignment_grades extends ListActivity {

    ArrayList gradesOnAssignments = new ArrayList<String>();
    protected static ArrayAdapter<String> arrayAdapter = null;

    String userId, courseCode, courseName;

    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grades);
        Intent intent = getIntent();


        dbref = FirebaseDatabase.getInstance().getReference();

        //Changes the header with the current course with the correct name
        TextView courseNameView = (TextView) findViewById(R.id.courseName);
        courseName = intent.getStringExtra("courseNameWithGrades");
        courseCode = courseName.substring(courseName.indexOf("(") + 1, courseName.indexOf(")"));
        courseNameView.setText(courseName);

        getUid();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gradesOnAssignments);
        getListView().setAdapter(arrayAdapter);

    }

    public void getUid() {
        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference  localRef = dbref.child("grades")
                                           .child(userId)
                                           .child("assignments")
                                           .child(courseCode);

        localRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String assignmentGrade = postSnapshot.getKey() + "\n" +
                            "Betyg: " + postSnapshot.getValue().toString();
                    gradesOnAssignments.add(assignmentGrade);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clearAdapter(){
        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearAdapter();
        arrayAdapter.notifyDataSetChanged();
    }
}
