package se.newton.scrummerz;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import se.newton.scrummerz.model.Assignment;

public class assignment extends ListActivity {

    DatabaseReference dbRef;
    String userId, courseKey, classKey;

    ArrayList assignments = new ArrayList<String>();
    protected static ArrayAdapter<String> arrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Intent intent = getIntent();


        TextView courseNameView;

        courseNameView = (TextView) findViewById(R.id.courseNameView);
        courseNameView.setText(intent.getStringExtra("courseName"));
        classKey = intent.getStringExtra("classKey");
        courseKey = intent.getStringExtra("courseKey");

        dbRef = FirebaseDatabase.getInstance().getReference();
        getUid();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, assignments);
        getListView().setAdapter(arrayAdapter);
        clearAdapter();
        arrayAdapter.notifyDataSetChanged();


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

        final DatabaseReference assignMentRef = dbRef.child("coursesByClass")
                                               .child(classKey)
                                               .child(courseKey)
                                               .child("assignments");

        assignMentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();

                    Log.i("test THIS", " " + postSnapshot.getValue().toString());
                    Assignment assignment = postSnapshot.getValue(Assignment.class);
                    Log.i("test THIS IS THE ONE", "" + assignment.getDuedate());


                    String assignmentTxt = postSnapshot.getKey() + "\n" +
                            assignment.getDuedate() + "\n" + assignment.getInfo();
                    assignments.add(assignmentTxt);


//                    assignMentRef.child(key).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//
//
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
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

}
