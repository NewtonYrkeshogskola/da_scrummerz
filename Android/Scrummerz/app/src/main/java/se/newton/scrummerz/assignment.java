package se.newton.scrummerz;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
        TextView courseNameView = (TextView) findViewById(R.id.courseNameView);

        Intent intent = getIntent();
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(assignment.this, assignment_info.class);
        String assignmentName = l.getItemAtPosition(position).toString();
        assignmentName = assignmentName.substring(0, assignmentName.indexOf("Slutdatum") - 1);
        intent.putExtra("assignmentName", assignmentName);
        intent.putExtra("classKey", classKey);
        intent.putExtra("courseKey", courseKey);
        l.getItemAtPosition(position);
        startActivity(intent);
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
        clearAdapter();

        final DatabaseReference assignMentRef = dbRef.child("coursesByClass")
                                               .child(classKey)
                                               .child(courseKey)
                                               .child("assignments");

        assignMentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Assignment assignment = postSnapshot.getValue(Assignment.class);
                    String year, month, day;
                    year = assignment.getDuedate().toString().substring(0, 4);
                    month = assignment.getDuedate().toString().substring(4, 6);
                    day = assignment.getDuedate().toString().substring(6);

                    String assignmentTxt = postSnapshot.getKey() + "\n" +
                            "Slutdatum: " + year + "-" + month + "-" + day;
                    assignments.add(assignmentTxt);
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
        clearAdapter();
        super.onResume();
    }

}
