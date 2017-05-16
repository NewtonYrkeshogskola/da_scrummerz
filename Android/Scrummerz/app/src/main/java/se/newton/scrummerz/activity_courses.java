package se.newton.scrummerz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.newton.scrummerz.model.Courses;
import se.newton.scrummerz.model.Student;

public class activity_courses extends AppCompatActivity {

    String userId, myClass, name;
    DatabaseReference dbRef;
    DatabaseReference classesRef;
    private RecyclerView allCourses;
    SharedPreferences studentInfo;
    private FirebaseRecyclerAdapter<Courses, ItemViewHolder> mAdapter = null;

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        myClass = savedInstanceState.getString("class");
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        studentInfo = PreferenceManager.getDefaultSharedPreferences(this);
        myClass = studentInfo.getString("studentClass", "");
        Log.i("TEST myClass", " " + myClass);
        dbRef = FirebaseDatabase.getInstance().getReference();
        getUid();
        classesRef = dbRef.child("coursesByClass").child(myClass);
        getMyClass(userId);

        allCourses = (RecyclerView) findViewById(R.id.coursesRecyclerView);
        allCourses.setHasFixedSize(false);
        allCourses.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();

        mAdapter = new FirebaseRecyclerAdapter<Courses, ItemViewHolder>(
                Courses.class,
                R.layout.item_courses,
                ItemViewHolder.class,
                classesRef) {

            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, final Courses model, int position) {
                viewHolder.setTitle(model.getName());
                viewHolder.setBody(model.getStatus());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Launch CourseInfo class
                        final Intent intent = new Intent(activity_courses.this, MainActivity.class);

                        String courseKey = model.getCourseCode();

                        intent.putExtra("courseKey", courseKey);
                        intent.putExtra("classKey", myClass);
                        startActivity(intent);

                    }
                });
            }

        };
        allCourses.setAdapter(mAdapter);
    }


    // Needs to be protected to work on all mobiles
    @SuppressWarnings("WeakerAccess")
    protected static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView itemTitle = (TextView) mView.findViewById(R.id.Item_title_courseTitle);
            itemTitle.setText(title);
        }

        void setBody(String body) {
            TextView itemBody = (TextView) mView.findViewById(R.id.Item_category);
            itemBody.setText(body);
        }
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
                myClass = student.myClass;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





}
