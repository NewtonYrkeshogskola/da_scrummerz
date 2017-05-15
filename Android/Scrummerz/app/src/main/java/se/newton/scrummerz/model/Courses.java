package se.newton.scrummerz.model;


public class Courses {
    public String myClass;

    private Boolean Active;
    private String CourseCode;
    private String Name;
    private String Teacher;

    public Courses() {
        // Default constructor
    }

    public Courses(String myClass, String Name, String Teacher, String CourseCode, Boolean Active) {
        this.Teacher = Teacher;
        this.Name = Name;
        this.myClass = myClass;
        this.Active = Active;
        this.CourseCode = CourseCode;
    }

    public String getTeacher() {
        return Teacher;
    }

    public String getName() {
        return Name;
    }

    public Boolean getActive() {
        return Active;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public String getmyClass() {
        return myClass;
    }



    public String setTeacher(String Teacher) {
        this.Teacher = Teacher;
    }

    public String setName(String Name) {
        this.Name = Name;
    }

    public Boolean setActive(Boolean Active) {
        this.Active = Active;
    }

    public String setCourseCode(String CourseCode) {
        this.CourseCode = CourseCode;
    }

}
