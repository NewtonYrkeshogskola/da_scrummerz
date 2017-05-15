package se.newton.scrummerz;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.newton.scrummerz.model.Student;

public class activity_courses extends AppCompatActivity {

    String userId, myClass;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        dbRef = FirebaseDatabase.getInstance().getReference();

        getUid();
        getMyClass(userId);
        getCourses();


    }


    public void getCourses(){
        DatabaseReference localRef = dbRef.child("classes").child(myClass).child("TestKurser");


    }

    public void getUid(){
        try{
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException ignored){}
    }


    public void getMyClass(String user){
        DatabaseReference localRef = dbRef.child("users").child("Pupils").child(user);

        localRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = new Student();
                student = dataSnapshot.getValue(Student.class);
                myClass = student.Class;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
