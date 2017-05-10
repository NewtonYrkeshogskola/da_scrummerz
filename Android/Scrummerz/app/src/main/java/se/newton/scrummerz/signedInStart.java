package se.newton.scrummerz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class signedInStart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference mRoot;

    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in_start);
        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        nameTextView = (TextView) findViewById(R.id.nameTextView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String uid = currentUser.getUid();

        DatabaseReference mStudent = mRoot.child("users").child("Pupils").child(uid);

        mStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = new Student();
                student = dataSnapshot.getValue(Student.class);
                nameTextView.setText(student.Name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
