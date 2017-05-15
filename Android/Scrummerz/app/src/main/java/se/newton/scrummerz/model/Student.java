package se.newton.scrummerz.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    private String Pnr;
    private String Name;
    public String Class;


    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String Class, String Name, String Pnr) {
        this.Pnr = Pnr;
        this.Name = Name;
        this.Class = Class;
    }

    public String getPnr() {
        return Pnr;
    }

    public String getName() {
        return Name;
    }

}

