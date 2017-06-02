package se.newton.scrummerz.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Finals {
    public String grade;
    public String name;


    public Finals() {
        // Default constructor required for calls to DataSnapshot.getValue(Finals.class)
    }

    public Finals(String Course, String Grade) {
        this.name = Course;
        this.grade = Grade;
    }

    public String getGrade() {
        return grade;
    }

    public String getCourse() {
        return name;
    }

}

