package se.newton.scrummerz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import se.newton.scrummerz.model.Courses;
import se.newton.scrummerz.model.Student;

public class activity_courses extends AppCompatActivity {


    DatabaseReference dbRef;
    DatabaseReference classesRef;

    String userId, myClass;

    SharedPreferences studentInfo;

    private RecyclerView allCourses;
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
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);
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
            protected void populateViewHolder(final ItemViewHolder viewHolder, final Courses model, int position) {
                String key = this.getRef(position).getKey();

                classesRef.child(key).child("details").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        ((TextView)viewHolder.itemView.findViewById(R.id.Item_title_courseTitle)).setText(name);

                        String body = dataSnapshot.child("status").getValue(String.class);

                        //TODO: fixa strängar
                        if (body.equals("finished")) {
                            Drawable id = getResources().getDrawable(R.drawable.finished);
                            viewHolder.setImage(id);
                            ((TextView)viewHolder.itemView.findViewById(R.id.Item_category)).setText("Kursen är färdig");
                        } else if (body.equals("comming")) {
                            Drawable id = getResources().getDrawable(R.drawable.future);
                            viewHolder.setImage(id);
                            ((TextView)viewHolder.itemView.findViewById(R.id.Item_category)).setText("Kursen är inte påbörjad");
                        } else if (body.equals("progress")) {
                            Drawable id = getResources().getDrawable(R.drawable.ongoing);
                            viewHolder.setImage(id);
                            ((TextView)viewHolder.itemView.findViewById(R.id.Item_category)).setText("Kursen pågår");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("FEL", databaseError.toString());
                    }
                });





                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Launch CourseInfo class
                        final Intent intent = new Intent(activity_courses.this, CourseInfo.class);

                        String courseKey = model.getCourseCode();
                        String details = model.getDescription();
                        String teacher = model.getTeacher();
                        String courseName = model.getName();
                        String status = model.getStatus();
                        String formattedStatus ="";

                        intent.putExtra("courseName", courseName);
                        intent.putExtra("status", formattedStatus);
                        intent.putExtra("teacher", teacher);
                        intent.putExtra("description", details);
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
            if (Objects.equals(body, "progress"))
                itemBody.setText("Kursen är påbörjad");
            if (Objects.equals(body, "finished"))
                itemBody.setText("Kursen är avslutad");
            if (Objects.equals(body, "comming"))
                itemBody.setText("Kursen är ännu inte startad");
        }

        void setImage (Drawable image) {
            ImageView imageView = (ImageView) mView.findViewById(R.id.course_status);
            imageView.setImageDrawable(image);
        }

    }


    public void getUid(){
        try{
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException ignored){}
    }


    public void getMyClass(String user){
        DatabaseReference localRef = dbRef.child("users").child("students").child(user).child("details");

        localRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                myClass = student.myClass;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





}
