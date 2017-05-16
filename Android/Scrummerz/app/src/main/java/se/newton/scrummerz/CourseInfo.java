package se.newton.scrummerz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseInfo extends AppCompatActivity {

    String details, teacher, courseName, status;

    DatabaseReference dbRef;
    DatabaseReference classesRef;
    private RecyclerView allCourses;
    SharedPreferences studentInfo;

    TextView courseNameView, detailsView, teacherView, statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        dbRef = FirebaseDatabase.getInstance().getReference();
        studentInfo = PreferenceManager.getDefaultSharedPreferences(this);

        courseNameView = (TextView) findViewById(R.id.courseName);
        detailsView = (TextView) findViewById(R.id.detailsView);
        teacherView = (TextView) findViewById(R.id.teacherView);
        statusView = (TextView) findViewById(R.id.statusView);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        details = intent.getStringExtra("description");
        teacher = intent.getStringExtra("teacher");
        status = intent.getStringExtra("status");

        detailsView.setText(details);
        courseNameView.setText(courseName);
        teacherView.setText(teacher);
        statusView.setText(status);


    }
}