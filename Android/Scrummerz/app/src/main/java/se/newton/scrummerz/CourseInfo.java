package se.newton.scrummerz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        final String details, teacher, courseName, status, courseKey, classKey;
        TextView courseNameView, detailsView, teacherView, statusView;
        Button assignmentBtn;

        courseNameView = (TextView) findViewById(R.id.courseName);
        detailsView = (TextView) findViewById(R.id.detailsView);
        teacherView = (TextView) findViewById(R.id.teacherView);
        statusView = (TextView) findViewById(R.id.statusView);
        assignmentBtn = (Button) findViewById(R.id.assignmentsBtn);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        details = intent.getStringExtra("description");
        teacher = intent.getStringExtra("teacher");
        status = intent.getStringExtra("status");
        courseKey = intent.getStringExtra("courseKey");
        classKey = intent.getStringExtra("classKey");

        detailsView.setText(details);
        courseNameView.setText(courseName);
        teacherView.setText(teacher);
        statusView.setText(status);

        assignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseInfo.this, assignment.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("courseKey", courseKey);
                intent.putExtra("classKey", classKey);
                startActivity(intent);
            }
        });


    }
}
