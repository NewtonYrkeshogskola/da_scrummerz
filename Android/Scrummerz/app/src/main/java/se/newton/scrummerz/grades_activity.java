package se.newton.scrummerz;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import se.newton.scrummerz.model.Courses;

public class grades_activity extends ListActivity {


    DatabaseReference dbRef;

    String userId, grade, courseCode;
    SharedPreferences preferences;
    ArrayList grades = new ArrayList<String>();
    protected static ArrayAdapter<String> arrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_activity);

        dbRef = FirebaseDatabase.getInstance().getReference();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        getUid();
        getMyGrades(userId);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grades);
        getListView().setAdapter(arrayAdapter);
        clearAdapter();
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(grades_activity.this, assignment_grades.class);
        String courseName = l.getItemAtPosition(position).toString();
        courseName = courseName.substring(0, courseName.indexOf("Slutbetyg") - 1);
        intent.putExtra("courseNameWithGrades", courseName);
        l.getItemAtPosition(position);
        startActivity(intent);
    }


    public void getUid() {
        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException ignored) {
        }
    }

    public void getMyGrades(String user) {
        DatabaseReference gradesRef = dbRef.child("grades").child(userId).child("final");
        final String myClass = preferences.getString("studentClass", "");
        Log.i("test class", myClass);

        gradesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    courseCode = postSnapshot.getKey();
                    grade = postSnapshot.getValue().toString();

                    DatabaseReference courseRef = dbRef.child("coursesByClass").child(myClass).child(courseCode).child("details");
                    courseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Courses courses = dataSnapshot.getValue(Courses.class);
                            grades.add(courses.getName() + " (" + courses.getCourseCode() + ")"
                                    + "\nSlutbetyg på denna kurs: " + grade);
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
