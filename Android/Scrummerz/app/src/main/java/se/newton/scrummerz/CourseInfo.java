package se.newton.scrummerz;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseInfo extends AppCompatActivity {

    String userId, myClass, name;
    DatabaseReference dbRef;
    DatabaseReference classesRef;
    private RecyclerView allCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        dbRef = FirebaseDatabase.getInstance().getReference();
        studentInfo = PreferenceManager.getDefaultSharedPreferences(this);

    }
}
