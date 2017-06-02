package se.newton.scrummerz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.newton.scrummerz.model.Assignment;

public class assignment_info extends AppCompatActivity {

    DatabaseReference dbRef;
    String courseKey, classKey, assignment, info;

    TextView assignmentInfoView, assignmentNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_info);
        dbRef = FirebaseDatabase.getInstance().getReference();

        assignmentInfoView = (TextView) findViewById(R.id.assignmentInfo);
        assignmentNameView = (TextView) findViewById(R.id.assignmentName);
        Intent intent = getIntent();
        courseKey = intent.getStringExtra("courseKey");
        classKey = intent.getStringExtra("classKey");
        assignment = intent.getStringExtra("assignmentName");

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference assigntmentInfoRef = dbRef.child("coursesByClass")
                .child(classKey)
                .child(courseKey)
                .child("assignments")
                .child(assignment);

        assigntmentInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Assignment assignment = dataSnapshot.getValue(Assignment.class);
                info = assignment.getInfo();
                assignmentNameView.setText("" + dataSnapshot.getKey());
                assignmentInfoView.setText(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
