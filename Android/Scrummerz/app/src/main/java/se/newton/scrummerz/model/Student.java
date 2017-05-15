package se.newton.scrummerz.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    private String Pnr;
    private String Name;
    public String myClass;


    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String myClass, String Name, String Pnr) {
        this.Pnr = Pnr;
        this.Name = Name;
        this.myClass = myClass;
    }

    public String getPnr() {
        return Pnr;
    }

    public String getName() {
        return Name;
    }

    public String getmyClass() {
        return myClass;
    }


    public void setPnr(String pnr) {
        Pnr = pnr;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMyClass() {
        return myClass;
    }

    public void setMyClass(String myClass) {
        this.myClass = myClass;
    }
}

