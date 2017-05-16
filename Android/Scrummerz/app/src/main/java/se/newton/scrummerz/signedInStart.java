package se.newton.scrummerz;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import se.newton.scrummerz.model.Student;

public class signedInStart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRoot;
    TextView nameTextView;
    String uid;
    Student student = new Student();
    SharedPreferences studentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in_start);
        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        uid = currentUser.getUid();
        studentInfo = PreferenceManager.getDefaultSharedPreferences(this);



        nameTextView = (TextView) findViewById(R.id.welcome);
        TextView coursesTextView = (TextView) findViewById(R.id.myCoursesTextView);

        coursesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signedInStart.this, activity_courses.class);
                intent.putExtra("class", student.myClass);
                startActivity(intent);
            }
        });

        Button minus = (Button) findViewById(R.id.minus);
        Button neutral = (Button) findViewById(R.id.neutral);
        Button plus = (Button) findViewById(R.id.plus);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        String classRef = studentInfo.getString("studentClass", "");
        final DatabaseReference dateData = mRoot.child("feelings").child(classRef).child(currentDate).child(uid);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateData.setValue(-1);
            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateData.setValue(0);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateData.setValue(1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mStudent = mRoot.child("users").child("students").child(uid).child("details");

        mStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                student = dataSnapshot.getValue(Student.class);
                nameTextView.setText("Välkommen " + student.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        studentInfo.edit().putString("studentName", student.getName()).apply();
        studentInfo.edit().putString("studentClass", student.getmyClass()).apply();
        studentInfo.edit().putString("studentPnr", student.getPnr()).apply();
        studentInfo.edit().putString("studentUid",currentUser.getUid()).apply();
    }

}
