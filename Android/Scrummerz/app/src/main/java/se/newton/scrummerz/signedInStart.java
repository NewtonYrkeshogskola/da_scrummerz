package se.newton.scrummerz;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import se.newton.scrummerz.model.Student;

public class signedInStart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRoot;

    TextView nameTextView, myGradesTextView;
    String uid;
    Student student = new Student();
    SharedPreferences studentInfo;
    RelativeLayout.LayoutParams layoutparams;

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
        myGradesTextView = (TextView) findViewById(R.id.myGradesTextView);
        TextView coursesTextView = (TextView) findViewById(R.id.myCoursesTextView);
        Button startScanner = (Button) findViewById(R.id.scanner);


        startScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signedInStart.this, scanner.class);
                startActivity(intent);
            }
        });


        //Starts the course activity
        coursesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signedInStart.this, activity_courses.class);
                intent.putExtra("class", student.myClass);
                startActivity(intent);
            }
        });

        //Starts the grades activity
        myGradesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signedInStart.this, grades_activity.class);
                startActivity(intent);
            }
        });

        final ImageButton minus = (ImageButton) findViewById(R.id.negative);
        final ImageButton neutral = (ImageButton) findViewById(R.id.neutral);
        final ImageButton plus = (ImageButton) findViewById(R.id.positive);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        String classRef = studentInfo.getString("studentClass", "");
        final DatabaseReference dateData = mRoot.child("feelings").child(classRef).child(currentDate).child(uid);
        dateData.keepSynced(true);

        //Width 70dp, height 68dp

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateData.setValue(-1);
                minus.setScaleX(1.15f);
                minus.setScaleY(1.15f);

                neutral.setScaleX(0.70f);
                neutral.setScaleY(0.70f);

                plus.setScaleX(0.70f);
                plus.setScaleY(0.70f);

                Toast.makeText(signedInStart.this, "Tack för din röst!", Toast.LENGTH_LONG).show();
            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateData.setValue(0);
                minus.setScaleX(0.70f);
                minus.setScaleY(0.70f);

                neutral.setScaleX(1.15f);
                neutral.setScaleY(1.15f);

                plus.setScaleX(0.70f);
                plus.setScaleY(0.70f);
                Toast.makeText(signedInStart.this, "Tack för din röst!", Toast.LENGTH_LONG).show();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateData.setValue(1);
                minus.setScaleX(0.70f);
                minus.setScaleY(0.70f);

                neutral.setScaleX(0.70f);
                neutral.setScaleY(0.70f);

                plus.setScaleX(1.15f);
                plus.setScaleY(1.15f);
                Toast.makeText(signedInStart.this, "Tack för din röst!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String welcomeText = "Välkommen " + studentInfo.getString("studentName", "");
        nameTextView.setText(welcomeText);
    }

}
