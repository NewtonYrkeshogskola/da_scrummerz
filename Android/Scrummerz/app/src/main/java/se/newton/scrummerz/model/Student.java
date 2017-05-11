package se.newton.scrummerz.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    public String Pnr;
    public String Name;
    public String Class;


    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String Pnr, String Name, String Class) {
        this.Pnr = Pnr;
        this.Name = Name;
        this.Class = Class;
    }
}

